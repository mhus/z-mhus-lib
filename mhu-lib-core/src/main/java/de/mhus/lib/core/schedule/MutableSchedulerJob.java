package de.mhus.lib.core.schedule;

public interface MutableSchedulerJob {

	void setDone(boolean done);
	void doReschedule(Scheduler scheduler, long time);
	/**
	 * Reconfigure the scheduler. Returns true if it was successful.
	 * 
	 * @param config
	 * @return
	 */
	boolean doReconfigure(String config);
	void setScheduledTime(long scheduledTime);

}
