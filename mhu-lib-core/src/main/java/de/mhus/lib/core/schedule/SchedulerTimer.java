package de.mhus.lib.core.schedule;

import java.util.Date;
import java.util.Observer;
import java.util.TimerTask;

import de.mhus.lib.core.util.TimerIfc;

public class SchedulerTimer extends Scheduler implements TimerIfc {

	public SchedulerTimer() {
		super();
	}

	public SchedulerTimer(String name) {
		super(name);
	}

	@Override
	public void schedule(TimerTask task, long delay) {
		schedule(new OnceJob(System.currentTimeMillis() + delay, new ObserverTimerTaskAdapter(task) ));
	}

	@Override
	public void schedule(TimerTask task, Date time) {
		schedule(new OnceJob(time, new ObserverTimerTaskAdapter(task) ));
	}

	@Override
	public void schedule(TimerTask task, long delay, long period) {
		schedule(new IntervalWithStartTimeJob(System.currentTimeMillis() + delay, period, new ObserverTimerTaskAdapter(task) ));
	}

	@Override
	public void schedule(TimerTask task, Date firstTime, long period) {
		schedule(new IntervalWithStartTimeJob(firstTime.getTime(), period, new ObserverTimerTaskAdapter(task) ));
	}

	@Override
	public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
		schedule(new IntervalWithStartTimeJob(System.currentTimeMillis() + delay, period, new ObserverTimerTaskAdapter(task) ));
	}

	@Override
	public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
		schedule(new IntervalWithStartTimeJob(firstTime.getTime(), period, new ObserverTimerTaskAdapter(task) ));
	}

	@Override
	public void cancel() {
		stop();
	}

}
