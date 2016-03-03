package de.mhus.lib.core.schedule;

import java.util.Date;
import java.util.Observer;
import java.util.TimerTask;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.util.TimerIfc;

/**
 * <p>SchedulerTimer class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class SchedulerTimer extends Scheduler implements TimerIfc {

	/**
	 * <p>Constructor for SchedulerTimer.</p>
	 */
	public SchedulerTimer() {
		super();
	}

	/**
	 * <p>Constructor for SchedulerTimer.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public SchedulerTimer(String name) {
		super(name);
	}

	/** {@inheritDoc} */
	@Override
	public void schedule(TimerTask task, long delay) {
		schedule(new OnceJob(System.currentTimeMillis() + delay, new ObserverTimerTaskAdapter(task) ));
	}

	/** {@inheritDoc} */
	@Override
	public void schedule(TimerTask task, Date time) {
		schedule(new OnceJob(time, new ObserverTimerTaskAdapter(task) ));
	}

	/** {@inheritDoc} */
	@Override
	public void schedule(TimerTask task, long delay, long period) {
		schedule(new IntervalWithStartTimeJob(System.currentTimeMillis() + delay, period, new ObserverTimerTaskAdapter(task) ));
	}

	/** {@inheritDoc} */
	@Override
	public void schedule(TimerTask task, Date firstTime, long period) {
		schedule(new IntervalWithStartTimeJob(firstTime.getTime(), period, new ObserverTimerTaskAdapter(task) ));
	}

	/** {@inheritDoc} */
	@Override
	public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
		schedule(new IntervalWithStartTimeJob(System.currentTimeMillis() + delay, period, new ObserverTimerTaskAdapter(task) ));
	}

	/** {@inheritDoc} */
	@Override
	public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
		schedule(new IntervalWithStartTimeJob(firstTime.getTime(), period, new ObserverTimerTaskAdapter(task) ));
	}

	/** {@inheritDoc} */
	@Override
	public void schedule(SchedulerJob job) {
		super.schedule(job);
		configureDefault(job);
	}
	
	/**
	 * <p>configureDefault.</p>
	 *
	 * @param job a {@link de.mhus.lib.core.schedule.SchedulerJob} object.
	 */
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
		MProperties properties = MProperties.load("etc/mhus_timer.properties"); // TODO configurable via mhu-config
		return properties;
	}

	/** {@inheritDoc} */
	@Override
	public void cancel() {
		stop();
	}

}
