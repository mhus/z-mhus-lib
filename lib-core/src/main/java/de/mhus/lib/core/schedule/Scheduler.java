/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.schedule;

import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.mhus.lib.basics.Named;
import de.mhus.lib.core.ITimerTask;
import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.lang.ValueProvider;
import de.mhus.lib.errors.TimeoutRuntimeException;

public class Scheduler extends MLog implements Named {

    private Timer timer;
    SchedulerQueue queue = new QueueList();
    private String name = Scheduler.class.getCanonicalName();
    protected LinkedList<SchedulerJob> running = new LinkedList<>();
    protected HashSet<SchedulerJob> jobs = new HashSet<>();
    private long nextTimeoutCheck;
    private long lastQueueCheck = System.currentTimeMillis();
    private long queueCheckTimeout = MPeriod.MINUTE_IN_MILLISECOUNDS;

    public Scheduler() {}

    public Scheduler(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void start() {
        if (timer != null) return;
        timer = new Timer(name, true);
        timer.scheduleAtFixedRate(
                new TimerTask() {

                    @Override
                    public void run() {
                        try {
                            doTick();
                        } catch (Throwable t) {
                            log().e(t);
                        }
                    }
                },
                1000,
                1000);
    }

    protected void doTick() {
        synchronized (jobs) {
            // check queue
            if (MPeriod.isTimeOut(lastQueueCheck, queueCheckTimeout)) doQueueCheck();

            // execute overdue jobs
            List<SchedulerJob> pack = queue.removeJobs(System.currentTimeMillis());
            if (pack != null) {
                for (SchedulerJob job : pack) {
                    try {
                        doExecuteJobInternal(job, false);
                    } catch (Throwable t) {
                        log().t("Job error", job.getName(), job, t);
                        job.doError(t);
                    }
                }
            }
        } // END LOCK

        // notify timeout of jobs
        long time = System.currentTimeMillis();
        if (nextTimeoutCheck < time) {
            try {
                for (SchedulerJob job : running) {
                    long timeout = job.getTimeoutInMinutes() * MPeriod.MINUTE_IN_MILLISECOUNDS;
                    if (timeout > 0 && timeout + job.getLastExecutionStart() <= time) {
                        try {
                            if (job.isBusy()) {
                                log().d("job timeout", job.getName());
                                job.doTimeoutReached();
                            }
                        } catch (Throwable t) {
                            job.doError(t);
                        }
                    }
                }
            } catch (ConcurrentModificationException cme) {
                // ignore if a job ends in the same time
            } catch (Throwable t) {
                log().e(t); // should not happen
            }

            nextTimeoutCheck = time + MPeriod.MINUTE_IN_MILLISECOUNDS;
        }
    }

    public void doExecuteJob(SchedulerJob job, boolean forced) {
        queue.removeJob(job);
        doExecuteJobInternal(job, forced);
    }

    protected void doExecuteJobInternal(SchedulerJob job, boolean forced) {
        if (!job.setBusy(this)) {
            log().w("job is busy, reshedule", job.getName());
            boolean isRunning = false;
            synchronized (running) {
                isRunning = running.contains(job);
            }
            try {
                if (!isRunning) {
                    log().w("release job lock", job.getName());
                    job.releaseBusy(null);
                } else {
                    log().w("found a running job in the scheduler queue", job.getName());
                    job.setNextExecutionTime(SchedulerJob.CALCULATE_NEXT);
                    job.doSchedule(this);
                }
            } catch (Throwable t) {
                log().e("busy error", job.getName(), job, t);
            }
            return;
        }
        new MThread(new MyExecutor(job, forced))
                .start(); // TODO unsafe, monitor runtime use timeout or long runtime warnings, use
                          // maximal number of threads. be sure a job is running once
    }

    public void stop() {
        if (timer == null) return;
        timer.cancel();
        timer = null;
    }

    public void schedule(SchedulerJob job) {
        synchronized (job) {
            jobs.add(job);
        }
        job.doSchedule(this);
    }

    public void doQueueCheck() {
        log().d("doQueueCheck");
        lastQueueCheck = System.currentTimeMillis();
        synchronized (running) {
            synchronized (jobs) {
                jobs.removeIf(
                        j -> {
                            return j.isCanceled();
                        });
                jobs.forEach(
                        j -> {
                            if (!queue.contains(j) && !running.contains(j)) {
                                try {
                                    log().w("reschedule lost job", j.getName());
                                    j.setNextExecutionTime(SchedulerJob.CALCULATE_NEXT);
                                    j.doSchedule(this);
                                } catch (Throwable t) {
                                    log().w("reschedule failed", j, t);
                                }
                            }
                        });
            }
        }
    }

    private class MyExecutor implements Runnable {

        private SchedulerJob job;
        private boolean forced;

        public MyExecutor(SchedulerJob job, boolean forced) {
            this.job = job;
            this.forced = forced;
        }

        @Override
        public void run() {
            log().d("Job started", job.getName());
            synchronized (running) {
                running.add(job);
            }
            try {
                if (job != null && !job.isCanceled()) {
                    log().d(">>> Tick", job.getName());
                    job.doTick(forced);
                    log().d("<<< Tick", job.getName());
                } else log().d("Job canceled", job.getName());
            } catch (Throwable t) {
                log().d(job.getName(), t);
                try {
                    job.doError(t);
                } catch (Throwable t2) {
                    try {
                        log().w("Error t2", job.getName(), t2); // should not happen
                    } catch (Throwable t3) {
                        t3.printStackTrace();
                    }
                }
            }

            synchronized (running) {
                try {
                    if (!job.releaseBusy(Scheduler.this)) {
                        log().w("Job release not possible, do hard release", job.getName());
                        job.releaseBusy(null);
                    }
                    running.remove(job);
                } catch (Throwable t1) {
                    log().f("Error t1", job.getName(), t1); // should not happen
                }
                try {
                    //	?			job.setNextExecutionTime(SchedulerJob.CALCULATE_NEXT);
                    job.doSchedule(Scheduler.this);
                } catch (Throwable t) {
                    log().f("Can't reschedule", job.getName(), t);
                }
            }
            try {
                if (job.isCanceled()) {
                    synchronized (jobs) {
                        jobs.remove(job);
                    }
                }
            } catch (Throwable t3) {
                log().f("Error t3", job.getName(), t3); // should not happen
            }
        }
    }

    public List<SchedulerJob> getRunningJobs() {
        synchronized (running) {
            return new LinkedList<>(running);
        }
    }

    public List<SchedulerJob> getJobs() {
        synchronized (jobs) {
            return new LinkedList<>(jobs);
        }
    }

    public List<SchedulerJob> getScheduledJobs() {
        return queue.getJobs();
    }

    public SchedulerQueue getQueue() {
        return queue;
    }

    public void clear() {
        try {
            MThread.getWithTimeout(
                    new ValueProvider<String>() {

                        @Override
                        public String getValue() throws Exception {
                            synchronized (running) {
                                if (running.isEmpty()) return "";
                                return null;
                            }
                        }
                    },
                    10000,
                    false);
        } catch (TimeoutRuntimeException e) {
            log().w("Can't stop running jobs");
        }
        jobs.clear();
        queue.clear();
    }

    /**
     * Create a job by definition. Format: once:date once:time cron:cron format interval:interval
     * cron interval
     *
     * <p>e.g. * * * * * 12000 once:12000
     *
     * @param interval
     * @param task
     * @return The job
     */
    public static SchedulerJob createSchedulerJob(String interval, ITimerTask task) {
        if (interval.startsWith("once:")) {
            interval = interval.substring(5);
            long s = 0;
            if (interval.indexOf('-') > 0 || interval.indexOf('.') > 0 || interval.indexOf('/') > 0)
                s = MDate.toDate(interval, new Date()).getTime();
            else s = System.currentTimeMillis() + MPeriod.toTime(interval, -1);
            return new OnceJob(s, task);
        } else if (interval.startsWith("cron:")) {
            return new CronJob(interval.substring(5), task);
        } else if (interval.startsWith("interval:")) {
            interval = interval.substring(9);
            return toIntervalJob(interval, task);
        } else if (interval.indexOf(' ') > 0) {
            return new CronJob(interval, task);
        } else {
            return toIntervalJob(interval, task);
        }
    }

    public static SchedulerJob toIntervalJob(String interval, ITimerTask task) {
        if (interval.indexOf(',') > 0) {
            long s = 0;
            String sStr = MString.beforeIndex(interval, ',');
            if (sStr.indexOf('-') > 0 || sStr.indexOf('.') > 0 || sStr.indexOf('/') > 0)
                // it's a date string
                s = MDate.toDate(sStr, new Date()).getTime();
            else
                // it should be a time interval
                s = System.currentTimeMillis() + MPeriod.toTime(sStr, -1);
            // delay is in every case a time interval
            long l = MPeriod.toTime(MString.afterIndex(interval, ','), -1);
            if (s > 0 && l > 0) return new IntervalWithStartTimeJob(s, l, task);
        } else {
            long l = MPeriod.toTime(interval, -1);
            if (l > 0) return new IntervalJob(l, task);
        }
        return null;
    }
}
