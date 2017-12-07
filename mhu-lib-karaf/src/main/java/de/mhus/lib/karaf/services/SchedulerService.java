package de.mhus.lib.karaf.services;

import java.util.Observer;

import de.mhus.lib.core.ITimerTask;
import de.mhus.lib.core.schedule.SchedulerJob;
import de.mhus.lib.core.schedule.TimerTaskIntercepter;

/**
 * Create this interface as a component and the timer will schedule the observer as a timer task.
 * Use the parameter 'interval' to define the default interval as time or cron job definition.
 * interval=15m - every 15 minutes from start (do not insert spaces!)
 * interval=1m,1h - first execution after 1 minute then every hour (do not insert spaces!)
 * interval=1,15,30,45 * * * * * Every 15 minutes exact every quarter hour
 * 
 * @author mikehummel
 *
 */
public interface SchedulerService extends ITimerTask {

	/**
	 * Overwrite interval defined in the component parameters. Return null if you don't need to
	 * define a customized interval.
	 * 
	 * @return
	 */
	String getInterval();

	SchedulerJob getWrappedJob();

	/**
	 * Return a intercepter to handle this
	 * @return
	 */
	TimerTaskIntercepter getIntercepter();
	
}
