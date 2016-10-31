package de.mhus.lib.core.schedule;

import java.util.Date;
import java.util.TimerTask;

import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MString;
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
	public void schedule(SchedulerJob job) {
		super.schedule(job);
		configureDefault(job);
	}
	
	public void configureDefault(SchedulerJob job) {
		MProperties properties = loadConfiguration();
		String n = job.getName();
		for (String key : properties.keys()) {
			if (MString.compareFsLikePattern(n, key)) {
				String v = properties.getString(key, null);
				if (v == null) continue;
				log().d("confiure by config file",n,key,v);
				if (v.equals("disabled"))
					job.doReschedule(this, SchedulerJob.DISABLED_TIME);
				else {
					((MutableSchedulerJob)job).doReconfigure(v);
					job.doReschedule(this, SchedulerJob.CALCULATE_NEXT);
				}
				return;
			}
		}
	}

	private MProperties loadConfiguration() {
		MProperties properties = MProperties.load(MSingleton.get().getSystemProperty(MConstants.PROP_TIMER_CONFIG_FILE, MConstants.DEFAULT_MHUS_TIMER_CONFIG_FILE)); // TODO configurable via mhu-config
		return properties;
	}

	@Override
	public void cancel() {
		stop();
	}

}
