package de.mhus.lib.core.schedule;

import de.mhus.lib.core.strategy.DefaultTaskContext;

public interface TimerTaskIntercepter {

	void initialize(SchedulerJob job);
	boolean beforeExecution(SchedulerJob job, DefaultTaskContext context, boolean forced);
	void afterExecution(SchedulerJob job, DefaultTaskContext context);
	void onError(SchedulerJob schedulerJob, DefaultTaskContext context, Throwable e);
}
