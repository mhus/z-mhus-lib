package de.mhus.lib.karaf.services;


import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerHack;
import java.util.TimerTask;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.TimerTaskSelfControl;
import de.mhus.lib.core.util.TimerFactory;
import de.mhus.lib.core.util.TimerIfc;

@Component(provide = TimerFactory.class, immediate=true,name="de.mhus.lib.karaf.services.TimerFactoryImpl")
public class TimerFactoryImpl implements TimerFactory {
	
	private Log log = Log.getLog(TimerFactoryImpl.class);
	private Timer myTimer;
	
	public TimerFactoryImpl() {
	}
	
	@Deactivate
	void doDeactivate() {
		myTimer.cancel();
		myTimer = null;
	}

	@Activate
	void doActivate() {
		myTimer = new Timer("de.mhus.lib.karaf.Timer", true);
	}

	
	private class TimerWrap implements TimerIfc {
		
		LinkedList<TimerTaskWrap> tasks = new LinkedList<>();
		
		@Override
		public void schedule(TimerTask task, long delay) {
			myTimer.schedule(new TimerTaskWrap(this, task), delay);
		}
	
		@Override
		public void schedule(TimerTask task, Date time) {
			myTimer.schedule(new TimerTaskWrap(this, task), time);
		}
	
		@Override
		public void schedule(TimerTask task, long delay, long period) {
			myTimer.schedule(new TimerTaskWrap(this, task), delay, period);
		}
	
		@Override
		public void schedule(TimerTask task, Date firstTime, long period) {
			myTimer.schedule(new TimerTaskWrap(this, task), firstTime, period);
		}
	
		@Override
		public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
			myTimer.scheduleAtFixedRate(new TimerTaskWrap(this, task), delay, period);
		}
	
		@Override
		public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
			myTimer.scheduleAtFixedRate(new TimerTaskWrap(this, task), firstTime, period);
		}

		@Override
		public void cancel() {
			synchronized (this) {
				for (TimerTaskWrap task : tasks)
					task.cancelDirect();
			}
		}

	}
	
	private class TimerTaskWrap extends TimerTask {

		private TimerTask task;
		private Bundle bundle;
		private TimerWrap timer;
		
		public TimerTaskWrap(TimerWrap timer, TimerTask task) {
			this.task = task;
			this.bundle = FrameworkUtil.getBundle(task.getClass());
			this.timer = timer;
			synchronized (timer) {
				timer.tasks.add(this);
			}
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
				if (task instanceof TimerTaskSelfControl) {
					if ( ((TimerTaskSelfControl)task).isCancelOnError() )
							cancel();
				} else
					cancel();
			}
		}
		
		@Override
		public boolean cancel() {
			synchronized (timer) {
				timer.tasks.remove(this);
			}
			return super.cancel();
		}
		
		private void cancelDirect() {
			super.cancel();
		}
		
	}

	@Override
	public TimerIfc getTimer() {
		return new TimerWrap();
	}
}