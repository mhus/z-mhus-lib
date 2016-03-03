package de.mhus.lib.core.util;

import java.util.Date;
import java.util.TimerTask;

import de.mhus.lib.annotations.activator.DefaultFactory;
import de.mhus.lib.core.schedule.SchedulerJob;

/**
 * <p>TimerIfc interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
@DefaultFactory(DefaultTimerFactory.class)
public interface TimerIfc {

	/**
	 * <p>schedule.</p>
	 *
	 * @param job a {@link de.mhus.lib.core.schedule.SchedulerJob} object.
	 */
	public void schedule(SchedulerJob job);
	
	/**
	 * <p>schedule.</p>
	 *
	 * @param task a {@link java.util.TimerTask} object.
	 * @param delay a long.
	 */
	public void schedule(TimerTask task, long delay);

	/**
	 * <p>schedule.</p>
	 *
	 * @param task a {@link java.util.TimerTask} object.
	 * @param time a {@link java.util.Date} object.
	 */
	public void schedule(TimerTask task, Date time);

	/**
	 * <p>schedule.</p>
	 *
	 * @param task a {@link java.util.TimerTask} object.
	 * @param delay a long.
	 * @param period a long.
	 */
	public void schedule(TimerTask task, long delay, long period);

	/**
	 * <p>schedule.</p>
	 *
	 * @param task a {@link java.util.TimerTask} object.
	 * @param firstTime a {@link java.util.Date} object.
	 * @param period a long.
	 */
	public void schedule(TimerTask task, Date firstTime, long period);

	/**
	 * <p>scheduleAtFixedRate.</p>
	 *
	 * @param task a {@link java.util.TimerTask} object.
	 * @param delay a long.
	 * @param period a long.
	 */
	public void scheduleAtFixedRate(TimerTask task, long delay, long period);

	/**
	 * <p>scheduleAtFixedRate.</p>
	 *
	 * @param task a {@link java.util.TimerTask} object.
	 * @param firstTime a {@link java.util.Date} object.
	 * @param period a long.
	 */
	public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period);

	/**
	 * <p>cancel.</p>
	 */
	public void cancel();

}
