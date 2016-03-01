package de.mhus.lib.core.schedule;

import java.util.Observer;

import de.mhus.lib.core.MCast;

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
		setNextExecutionTime(System.currentTimeMillis() + interval);
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

	@Override
	public boolean doReconfigure(String config) {
		long l = MCast.tolong(config, -1);
		if (l > 0 ) {
			interval = l;
			return true;
		}
		return false;
	}

	@Override
	public void setScheduledTime(long scheduledTime) {
		super.setScheduledTime(scheduledTime);
	}

}
