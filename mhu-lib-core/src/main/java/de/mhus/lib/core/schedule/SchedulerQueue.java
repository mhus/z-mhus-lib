package de.mhus.lib.core.schedule;

import java.util.List;

/**
 * Interface to a scheduler queue.  Queue must be synchronized!
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public interface SchedulerQueue {

	/**
	 * <p>removeJobs.</p>
	 *
	 * @param toTime a long.
	 * @return a {@link java.util.List} object.
	 */
	List<SchedulerJob> removeJobs(long toTime);

	/**
	 * <p>doSchedule.</p>
	 *
	 * @param job a {@link de.mhus.lib.core.schedule.SchedulerJob} object.
	 */
	void doSchedule(SchedulerJob job);

	/**
	 * <p>removeJob.</p>
	 *
	 * @param job a {@link de.mhus.lib.core.schedule.SchedulerJob} object.
	 */
	void removeJob(SchedulerJob job);

	/**
	 * <p>size.</p>
	 *
	 * @return a int.
	 */
	int size();
	
	/**
	 * <p>getJobs.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	List<SchedulerJob> getJobs();
	
	
}
