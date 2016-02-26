package de.mhus.lib.karaf.services;


import java.util.Date;
import java.util.LinkedList;
import java.util.TimerTask;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import de.mhus.lib.basics.Named;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MTimerTask;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.schedule.SchedulerJob;
import de.mhus.lib.core.schedule.SchedulerJobProxy;
import de.mhus.lib.core.schedule.SchedulerTimer;
import de.mhus.lib.core.util.TimerFactory;
import de.mhus.lib.core.util.TimerIfc;
import de.mhus.lib.core.util.TimerTaskSelfControl;
import de.mhus.lib.karaf.MOsgi;

@Component(provide = TimerFactory.class, immediate=true,name="de.mhus.lib.karaf.services.TimerFactoryImpl")
public class TimerFactoryImpl implements TimerFactory {
	
	private Log log = Log.getLog(TimerFactoryImpl.class);
	private SchedulerTimer myTimer;
//	private TreeMap<Long, MTimerTask> queue = new TreeMap<>();
	
	public TimerFactoryImpl() {
	}
	
	@Deactivate
	void doDeactivate(ComponentContext ctx) {
		log.i("cancel common timer");
		myTimer.cancel();
		myTimer = null;
	}

	@Activate
	void doActivate(ComponentContext ctx) {
		log.i("start common timer");
		myTimer = new SchedulerTimer("de.mhus.lib.karaf.Scheduler");
		myTimer.start();
		
		// set to base
		try {
			TimerFactory timerFactory = this;
			TimerIfc timerIfc = timerFactory.getTimer();
			MSingleton.get().getBaseControl().getCurrentBase().addObject(TimerIfc.class, timerIfc);
		} catch (Throwable t) {
			System.out.println("Can't initialize timer base: " + t);
		}

		
//		myTimer.schedule(new TimerTask() {
//			
//			@Override
//			public void run() {
//				doTick();
//			}
//		}, 1000, 1000); // tick every second
	}

	
//	protected void doTick() {
//		
//	}

	public static SchedulerTimer getScheduler(TimerFactory factory) {
		TimerIfc timer = factory.getTimer();
		if (timer instanceof TimerWrap) {
			return ((TimerWrap)timer).getScheduler();
		}
		return null;
	}

	private class TimerWrap implements TimerIfc {
		
		LinkedList<Wrap> tasks = new LinkedList<>();
		
		@Override
		public void schedule(TimerTask task, long delay) {
			myTimer.schedule(new TimerTaskWrap(this, task), delay);
		}
	
		public SchedulerTimer getScheduler() {
			return myTimer;
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
		public void schedule(SchedulerJob job) {
			myTimer.schedule(new SchedulerJobWrap(this, job) );
		}

		@Override
		public void cancel() {
			synchronized (this) {
				for (Wrap task : tasks)
					task.cancelDirect();
			}
		}

	}

	private interface Wrap {

		void cancelDirect();
		
	}
	
	private class TimerTaskWrap extends MTimerTask implements Wrap {

		private TimerTask task;
		private Bundle bundle;
		private long modified = 0;
		private TimerWrap timer;
		private BundleContext bundleContext;
		
		public TimerTaskWrap(TimerWrap timer, TimerTask task) {
			
			if (task != null && task instanceof Named)
				setName(((Named)task).getName());
			else
				setName(task.getClass().getCanonicalName());
			
			this.task = task;
			this.bundle = FrameworkUtil.getBundle(task.getClass());
			this.bundleContext = bundle.getBundleContext();
			this.modified = bundle.getLastModified();
			this.timer = timer;
			synchronized (timer) {
				timer.tasks.add(this);
			}
		}
		@Override
		public void doit() {
			try {
//				if (DefaultTimerFactory.isCancelled(task)) {
//					cancel();
//					return;
//				}
				if (bundle.getState() != Bundle.ACTIVE || bundle.getLastModified() != modified || bundleContext != bundle.getBundleContext()) {
					log.d("stop timertask",bundle.getBundleId(),bundle.getSymbolicName(),task.getClass().getCanonicalName());
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
		
		@Override
		public void cancelDirect() {
			super.cancel();
		}
		
		@Override
		public String toString() {
			return "[" + bundle.getBundleId() + ":" + bundle.getSymbolicName() + "]" + (task == null ? "null" : task.toString());
		}
	}
	
	private class SchedulerJobWrap extends SchedulerJobProxy implements Wrap {

		private Bundle bundle;
		private long modified = 0;
		private TimerWrap timer;
		private BundleContext bundleContext;

		public SchedulerJobWrap(TimerWrap timer, SchedulerJob task) {
			super(task);
			this.bundle = FrameworkUtil.getBundle(task.getClass());
			this.modified = bundle.getLastModified();
			this.bundleContext = bundle.getBundleContext();
			this.timer = timer;
			synchronized (timer) {
				timer.tasks.add(this);
			}
		}

		@Override
		public void doTick() {
			
			if (bundle.getState() != Bundle.ACTIVE || bundle.getLastModified() != modified || bundleContext != bundle.getBundleContext()) {
				log.d("stop scheduled task",bundle.getBundleId(),bundle.getSymbolicName(),getTask().getClass().getCanonicalName());
				cancel();
				return;
			}

			super.doTick();
		}
		
		@Override
		public void cancelDirect() {
			cancel();
		}
		
		@Override
		public void setCanceled(boolean canceled) {
			super.setCanceled(canceled);
			if (canceled) {
				synchronized (timer) {
					timer.tasks.remove(this);
					if (timer.getScheduler() != null) {
						//while (timer.getScheduler().getScheduledJobs().contains(this))
							timer.getScheduler().getQueue().removeJob(this);
					}
				}
			}
		}

		@Override
		public String toString() {
			return "[" + bundle.getBundleId() + ":" + bundle.getSymbolicName() + "]" + super.toString();
		}

		@Override
		public void setScheduledTime(long scheduledTime) {
			super.setScheduledTime(scheduledTime);
		}

	}

	@Override
	public TimerIfc getTimer() {
		return new TimerWrap();
	}
	
}