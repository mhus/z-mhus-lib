package de.mhus.lib.core.schedule;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.mhus.lib.core.MThread;

public class Scheduler {

	private Timer timer;
	SchedulerQueue queue = new QueueList();
	private String name = Scheduler.class.getCanonicalName();
	
	public Scheduler() {}
	
	public Scheduler(String name) {
		this.name = name;
	}
	
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
			if (pack == null) return;
			for (SchedulerJob job : pack) {
				try {
					doExecuteJob(job);
				} catch (Throwable t) {
					job.doError(t);
				}
				job.doSchedule(queue);
			}
	}

	protected void doExecuteJob(SchedulerJob job) {
		new MThread(job).start(); //TODO unsafe, monitor runtime use timeout or long runtime warnings, use maximal number of threads. be sure a job is running once
	}

	public void stop() {
		if (timer == null) return;
		timer.cancel();
		timer = null;
	}
	
	public void schedule(SchedulerJob scheduler) {
		scheduler.doSchedule(queue);
	}
	
}
