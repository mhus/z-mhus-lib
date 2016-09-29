package de.mhus.lib.core.schedule;

import java.util.Date;
import java.util.Observer;

import de.mhus.lib.core.MDate;

/**
 * <p>OnceJob class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class OnceJob extends SchedulerJob implements MutableSchedulerJob {

	private long time;

	/**
	 * <p>Constructor for OnceJob.</p>
	 *
	 * @param time a {@link java.util.Date} object.
	 * @param task a {@link java.util.Observer} object.
	 */
	public OnceJob(Date time, Observer task) {
		this(time.getTime(), task);
	}
	
	/**
	 * <p>Constructor for OnceJob.</p>
	 *
	 * @param time a long.
	 * @param task a {@link java.util.Observer} object.
	 */
	public OnceJob(long time, Observer task) {
		super(task);
		this.time = time;
	}

	/** {@inheritDoc} */
	@Override
	public void doCaclulateNextExecution() {
		if (isDone())
			setNextExecutionTime(REMOVE_TIME);
		else
			setNextExecutionTime(time);
	}

	/** {@inheritDoc} */
	@Override
	public void setDone(boolean done) {
		super.setDone(done);
		if(done)
			cancel();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return OnceJob.class.getSimpleName() + "," + isDone() + "," + MDate.toIsoDateTime(time);
	}

	/** {@inheritDoc} */
	@Override
	public void doReschedule(Scheduler queue, long time) {
		super.doReschedule(queue, time);
	}

	/** {@inheritDoc} */
	@Override
	public boolean doReconfigure(String config) {
		return false;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setScheduledTime(long scheduledTime) {
		super.setScheduledTime(scheduledTime);
	}

}
