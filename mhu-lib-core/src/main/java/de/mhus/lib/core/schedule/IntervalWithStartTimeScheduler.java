package de.mhus.lib.core.schedule;

import java.util.Observer;

/**
 * Executes the task every 'interval' milliseconds after execution finished.
 * @author mikehummel
 *
 */
public class IntervalWithStartTimeScheduler extends Scheduler {

	private long interval;
	private long start;

	public IntervalWithStartTimeScheduler(long start, long interval, Observer task) {
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

}
