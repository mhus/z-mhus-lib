package de.mhus.lib.core.schedule;

public interface MutableSchedulerJob {

	void setDone(boolean done);
	void doReschedule(Scheduler scheduler, long time);
	
}
