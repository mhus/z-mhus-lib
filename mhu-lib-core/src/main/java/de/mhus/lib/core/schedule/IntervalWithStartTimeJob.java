package de.mhus.lib.core.schedule;

import java.util.Observer;

import de.mhus.lib.core.MDate;

/**
 * Executes the task every 'interval' milliseconds after execution finished.
 * @author mikehummel
 *
 */
public class IntervalWithStartTimeJob extends SchedulerJob {

	private long interval;
	private long start;

	public IntervalWithStartTimeJob(long start, long interval, Observer task) {
		super(task);
		this.start = start;
		this.interval = interval;
	}

	@Override
	public void doCaclulateNextExecution() {
		if (isDone())
			nextExecutionTime = System.currentTimeMillis() + interval;
		else
			nextExecutionTime = start;
	}

	@Override
	public String toString() {
		return 
			IntervalWithStartTimeJob.class.getSimpleName() + "," + MDate.toIsoDateTime(start) + "," + interval;
	}

}
