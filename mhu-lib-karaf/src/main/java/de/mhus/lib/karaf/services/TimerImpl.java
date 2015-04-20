package de.mhus.lib.karaf.services;


import java.util.Date;
import java.util.Timer;
import java.util.TimerHack;
import java.util.TimerTask;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import de.mhus.lib.core.MTimerTask;
import de.mhus.lib.core.logging.Log;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;

@Component(provide = java.util.Timer.class, immediate=true,name="java.util.Timer")
public class TimerImpl extends java.util.Timer {
	
	private Log log = Log.getLog(TimerImpl.class);

	public TimerImpl() {
		super("de.mhus.lib.karaf.Timer", true);
	}
	
	@Deactivate
	void doDeactivate() {
		super.cancel();
	}

	@Activate
	void doActivate() {
	}

	@Override
	public void schedule(TimerTask task, long delay) {
		super.schedule(wrap(task), delay);
	}

	@Override
	public void schedule(TimerTask task, Date time) {
		super.schedule(wrap(task), time);
	}

	@Override
	public void schedule(TimerTask task, long delay, long period) {
		super.schedule(wrap(task), delay, period);
	}

	@Override
	public void schedule(TimerTask task, Date firstTime, long period) {
		super.schedule(wrap(task), firstTime, period);
	}

	@Override
	public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
		super.scheduleAtFixedRate(wrap(task), delay, period);
	}

	@Override
	public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
		super.scheduleAtFixedRate(wrap(task), firstTime, period);
	}

	/*
	 * Run the timers tasks in a background thread and not directly since this
	 * timer is shared of course.
	 * @param task
	 * @return
	 */
	private TimerTask wrap(final TimerTask task) {
		return new TimerWrap(task);
	}

	@Override
	public void cancel() {
		throw new UnsupportedOperationException("This is a shared timer, you cannot cancel such timers");
	}

	@Override
	public int purge() {
		throw new UnsupportedOperationException("This is a shared timer, you cannot purge such timers");
	}

	private class TimerWrap extends TimerTask {

		private TimerTask task;
		private Bundle bundle;
		private boolean cancelOnError = true;
		
		public TimerWrap(TimerTask task) {
			this.task = task;
			this.bundle = FrameworkUtil.getBundle(task.getClass());
		}
		@Override
		public void run() {
			try {
				if (TimerHack.isCancelled(task)) {
					cancel();
					return;
				}
				if (bundle.getState() != Bundle.ACTIVE) {
					log.d("stop task",bundle.getBundleId(),bundle.getSymbolicName(),task.getClass().getCanonicalName());
					cancel();
					return;
				}
				task.run();
			} catch (Throwable t) {
				log.i("error",bundle.getBundleId(),bundle.getSymbolicName(),task.getClass().getCanonicalName(), t);
				if (task instanceof ExtendedTimer) {
					if ( ((ExtendedTimer)task).isCancelOnError() )
							cancel();
				}
				if (cancelOnError)
					cancel();
			}
		}
		
	}
}