package de.mhus.lib.core.schedule;

import java.util.List;

/**
 * Interface to a scheduler queue.  Queue must be synchronized!
 * 
 * @author mikehummel
 *
 */
public interface SchedulerQueue {

	List<SchedulerJob> removeJobs(long toTime);

	void doSchedule(SchedulerJob job);

	void removeJob(SchedulerJob job);

	int size();
	
	List<SchedulerJob> getJobs();
	
	
}
