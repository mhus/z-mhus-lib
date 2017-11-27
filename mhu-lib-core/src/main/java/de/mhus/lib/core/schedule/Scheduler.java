package de.mhus.lib.core.schedule;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.mhus.lib.basics.Named;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.MTimeInterval;

public class Scheduler extends MLog implements Named {

	private Timer timer;
	SchedulerQueue queue = new QueueList();
	private String name = Scheduler.class.getCanonicalName();
	private LinkedList<SchedulerJob> running = new LinkedList<>();
	private long nextTimeoutCheck;
	
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
		timer = new Timer(name,true);
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				doTick();
			}
		}, 1000, 1000);
	}
	
	protected void doTick() {
		List<SchedulerJob> pack = queue.removeJobs(System.currentTimeMillis());
		if (pack != null) {
			for (SchedulerJob job : pack) {
				try {
					doExecuteJob(job, false);
				} catch (Throwable t) {
					job.doError(t);
				}
			}
		}
		
		long time = System.currentTimeMillis();
		if (nextTimeoutCheck < time) {
			synchronized (running) {
				try {
					for (SchedulerJob job : running) {
						long timeout = job.getTimeoutInMinutes() * MTimeInterval.MINUTE_IN_MILLISECOUNDS;
						if (timeout > 0 && timeout + job.getLastExecutionStart() <= time) {
							try {
								if (job.isBusy())
									job.doTimeoutReached();
							} catch (Throwable t) {
								job.doError(t);
							}
						}
					}
				} catch (ConcurrentModificationException cme) {}
				nextTimeoutCheck = time + MTimeInterval.MINUTE_IN_MILLISECOUNDS;
					
			}
		}
	}

	public void doExecuteJob(SchedulerJob job, boolean forced) {
		if (!job.setBusy(this)) {
			log().d("job is busy, reshedule",job);
			try {
				job.doSchedule(this);
			} catch (Throwable t) {
				job.doError(t);
			}
			return;
		}
		new MThread(new MyExecutor(job,forced)).start(); //TODO unsafe, monitor runtime use timeout or long runtime warnings, use maximal number of threads. be sure a job is running once
	}

	public void stop() {
		if (timer == null) return;
		timer.cancel();
		timer = null;
	}
	
	public void schedule(SchedulerJob scheduler) {
		scheduler.doSchedule(this);
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
			synchronized (running) {
				running.add(job);
			}
			try {
				if (job != null && !job.isCanceled())
					job.doTick(forced);
			} catch (Throwable t) {
				job.doError(t);
			} finally {
				try {
					synchronized (running) {
						running.remove(job);
					}
					job.releaseBusy(Scheduler.this);
				} catch (Throwable t1) {
					log().w(job,t1);
				}
			}
			try {
				job.doSchedule(Scheduler.this);
			} catch (Throwable t) {
				log().e(job,t);
			}
		}
		
	}

	
	public List<SchedulerJob> getRunningJobs() {
		synchronized (running) {
			return new LinkedList<>(running);
		}
	}
	
	public List<SchedulerJob> getScheduledJobs() {
		return queue.getJobs();
	}
	
	public SchedulerQueue getQueue() {
		return queue;
	}

}
