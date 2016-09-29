package de.mhus.lib.core.schedule;

import java.util.Observer;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MDate;

/**
 * Executes the task every 'interval' milliseconds after execution finished.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class IntervalWithStartTimeJob extends SchedulerJob implements MutableSchedulerJob {

	private long interval;
	private long start;

	/**
	 * <p>Constructor for IntervalWithStartTimeJob.</p>
	 *
	 * @param start a long.
	 * @param interval a long.
	 * @param task a {@link java.util.Observer} object.
	 */
	public IntervalWithStartTimeJob(long start, long interval, Observer task) {
		super(task);
		this.start = start;
		this.interval = interval;
	}

	/** {@inheritDoc} */
	@Override
	public void doCaclulateNextExecution() {
		if (isDone())
			setNextExecutionTime(System.currentTimeMillis() + interval);
		else
			setNextExecutionTime(start);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return 
			IntervalWithStartTimeJob.class.getSimpleName() + "," + MDate.toIsoDateTime(start) + "," + interval;
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
