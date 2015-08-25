package de.mhus.lib.core.schedule;

import java.util.Observer;

/**
 * Executes the task every 'interval' milliseconds after execution finished.
 * @author mikehummel
 *
 */
public class IntervalJob extends SchedulerJob implements MutableSchedulerJob {

	private long interval;

	public IntervalJob(long interval, Observer task) {
		super(task);
		this.interval = interval;
	}

	@Override
	public void doCaclulateNextExecution() {
		nextExecutionTime = System.currentTimeMillis() + interval;
	}

	@Override
	public String toString() {
		return 
			IntervalJob.class.getSimpleName() + "," + interval;
	}

	@Override
	public void doReschedule(Scheduler queue, long time) {
		super.doReschedule(queue, time);
	}

	@Override
	public void setDone(boolean done) {
		super.setDone(done);
	}

}
