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

import java.util.Date;
import java.util.TimerTask;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.base.service.TimerIfc;

public class SchedulerTimer extends Scheduler implements TimerIfc {

    public SchedulerTimer() {
        super();
    }

    public SchedulerTimer(String name) {
        super(name);
    }

    @Override
    public void schedule(TimerTask task, long delay) {
        schedule(new OnceJob(System.currentTimeMillis() + delay, new TimerTaskAdapter(task)));
    }

    @Override
    public void schedule(TimerTask task, Date time) {
        schedule(new OnceJob(time, new TimerTaskAdapter(task)));
    }

    @Override
    public void schedule(TimerTask task, long delay, long period) {
        schedule(
                new IntervalWithStartTimeJob(
                        System.currentTimeMillis() + delay, period, new TimerTaskAdapter(task)));
    }

    @Override
    public void schedule(TimerTask task, Date firstTime, long period) {
        schedule(
                new IntervalWithStartTimeJob(
                        firstTime.getTime(), period, new TimerTaskAdapter(task)));
    }

    @Override
    public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
        schedule(
                new IntervalWithStartTimeJob(
                        System.currentTimeMillis() + delay, period, new TimerTaskAdapter(task)));
    }

    @Override
    public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
        schedule(
                new IntervalWithStartTimeJob(
                        firstTime.getTime(), period, new TimerTaskAdapter(task)));
    }

    @Override
    public void schedule(String name, TimerTask task, long delay) {
        schedule(new OnceJob(System.currentTimeMillis() + delay, new TimerTaskAdapter(name, task)));
    }

    @Override
    public void schedule(String name, TimerTask task, Date time) {
        schedule(new OnceJob(time, new TimerTaskAdapter(name, task)));
    }

    @Override
    public void schedule(String name, TimerTask task, long delay, long period) {
        schedule(
                new IntervalWithStartTimeJob(
                        System.currentTimeMillis() + delay,
                        period,
                        new TimerTaskAdapter(name, task)));
    }

    @Override
    public void schedule(String name, TimerTask task, Date firstTime, long period) {
        schedule(
                new IntervalWithStartTimeJob(
                        firstTime.getTime(), period, new TimerTaskAdapter(name, task)));
    }

    @Override
    public void scheduleAtFixedRate(String name, TimerTask task, long delay, long period) {
        schedule(
                new IntervalWithStartTimeJob(
                        System.currentTimeMillis() + delay,
                        period,
                        new TimerTaskAdapter(name, task)));
    }

    @Override
    public void scheduleAtFixedRate(String name, TimerTask task, Date firstTime, long period) {
        schedule(
                new IntervalWithStartTimeJob(
                        firstTime.getTime(), period, new TimerTaskAdapter(name, task)));
    }

    @Override
    public void schedule(SchedulerJob job) {
        super.schedule(job);
        configureDefault(job);
    }

    public void configureDefault(SchedulerJob job) {
        MProperties properties = loadConfiguration();
        String n = job.getName();
        for (String key : properties.keys()) {
            if (MString.compareFsLikePattern(n, key)) {
                String v = properties.getString(key, null);
                if (v == null) continue;
                log().d("configure by config file", n, key, v);
                String[] vv = v.split("\\|");
                if (vv.length > 0 && vv[0].length() > 0) {
                    if (vv[0].equals("disabled"))
                        job.doReschedule(this, SchedulerJob.DISABLED_TIME);
                    else {
                        ((MutableSchedulerJob) job).doReconfigure(vv[0]);
                        job.doReschedule(this, SchedulerJob.CALCULATE_NEXT);
                    }
                }
                if (vv.length > 1 && vv[1].length() > 0) {
                    job.setLogTrailConfig(vv[1]);
                }
                return;
            }
        }
    }

    private MProperties loadConfiguration() {
        MProperties properties =
                MProperties.load(
                        MApi.getSystemProperty(MConstants.PROP_TIMER_CONFIG_FILE, null));
        return properties;
    }

    @Override
    public void cancel() {
        stop();
    }

    public void removeJob(SchedulerJob job) {
        getQueue().removeJob(job);
        synchronized (running) {
            running.remove(job);
        }
        synchronized (jobs) {
            jobs.remove(job);
        }
    }
}
