package de.mhus.lib.core.schedule;

import java.util.Observer;

import de.mhus.lib.core.MCast;

/**
 * Executes the task every 'interval' milliseconds after execution finished.
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class IntervalJob extends SchedulerJob implements MutableSchedulerJob {

	private long interval;

	/**
	 * <p>Constructor for IntervalJob.</p>
	 *
	 * @param interval a long.
	 * @param task a {@link java.util.Observer} object.
	 */
	public IntervalJob(long interval, Observer task) {
		super(task);
		this.interval = interval;
	}

	/** {@inheritDoc} */
	@Override
	public void doCaclulateNextExecution() {
		setNextExecutionTime(System.currentTimeMillis() + interval);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return 
			IntervalJob.class.getSimpleName() + "," + interval;
	}

	/** {@inheritDoc} */
	@Override
	public void doReschedule(Scheduler queue, long time) {
		super.doReschedule(queue, time);
	}

	/** {@inheritDoc} */
	@Override
	public void setDone(boolean done) {
		super.setDone(done);
	}

	/** {@inheritDoc} */
	@Override
	public boolean doReconfigure(String config) {
		long l = MCast.tolong(config, -1);
		if (l > 0 ) {
			interval = l;
			return true;
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void setScheduledTime(long scheduledTime) {
		super.setScheduledTime(scheduledTime);
	}

}
