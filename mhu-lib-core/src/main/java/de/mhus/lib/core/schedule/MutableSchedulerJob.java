package de.mhus.lib.core.schedule;

/**
 * <p>MutableSchedulerJob interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public interface MutableSchedulerJob {

	/**
	 * <p>setDone.</p>
	 *
	 * @param done a boolean.
	 */
	void setDone(boolean done);
	/**
	 * <p>doReschedule.</p>
	 *
	 * @param scheduler a {@link de.mhus.lib.core.schedule.Scheduler} object.
	 * @param time a long.
	 */
	void doReschedule(Scheduler scheduler, long time);
	/**
	 * Reconfigure the scheduler. Returns true if it was successful.
	 *
	 * @param config a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	boolean doReconfigure(String config);
	/**
	 * <p>setScheduledTime.</p>
	 *
	 * @param scheduledTime a long.
	 */
	void setScheduledTime(long scheduledTime);

}
