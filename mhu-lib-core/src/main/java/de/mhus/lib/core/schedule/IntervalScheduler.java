package de.mhus.lib.core.schedule;

import java.util.Observer;

/**
 * Executes the task every 'interval' milliseconds after execution finished.
 * @author mikehummel
 *
 */
public class IntervalScheduler extends Scheduler {

	private long interval;

	public IntervalScheduler(long interval, Observer task) {
		super(task);
		this.interval = interval;
	}

	@Override
	public void doCaclulateNextExecution() {
		nextExecutionTime = System.currentTimeMillis() + interval;
	}

}
