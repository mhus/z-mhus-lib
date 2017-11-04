package de.mhus.lib.karaf.services;


import java.util.Date;
import java.util.LinkedList;
import java.util.Observer;
import java.util.TimerTask;
import java.util.WeakHashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import de.mhus.lib.basics.Named;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.MTimerTask;
import de.mhus.lib.core.base.service.TimerFactory;
import de.mhus.lib.core.base.service.TimerIfc;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.core.schedule.CronJob;
import de.mhus.lib.core.schedule.IntervalJob;
import de.mhus.lib.core.schedule.IntervalWithStartTimeJob;
import de.mhus.lib.core.schedule.SchedulerJob;
import de.mhus.lib.core.schedule.SchedulerJobProxy;
import de.mhus.lib.core.schedule.SchedulerTimer;
import de.mhus.lib.core.util.TimerTaskSelfControl;
import de.mhus.lib.karaf.MServiceTracker;

@Component(provide = TimerFactory.class, immediate=true,name="de.mhus.lib.karaf.services.TimerFactoryImpl")
public class TimerFactoryImpl extends MLog implements TimerFactory {
	
	private SchedulerTimer myTimer = new SchedulerTimer("de.mhus.lib.karaf.Scheduler");
	private MServiceTracker<ScheduledService> tracker;
	private WeakHashMap<ScheduledService,TimerTask> services = new WeakHashMap<>();
	static TimerFactoryImpl instance;
	
	
		
	public TimerFactoryImpl() {
	}
	
	@Deactivate
	void doDeactivate(ComponentContext ctx) {
		
		MLogUtil.log().i("cancel common timer");
		tracker.stop();
		myTimer.cancel();
		myTimer = null;
		instance = null;
	}

	@Activate
	void doActivate(ComponentContext ctx) {
		
		instance = this;
		
		MLogUtil.log().i("start common timer");
		myTimer.start();
		
		// set to base
		try {
			TimerIfc timerIfc = new TimerWrap();
			MApi.get().getBaseControl().getCurrentBase().addObject(TimerIfc.class, timerIfc);
		} catch (Throwable t) {
			System.out.println("Can't initialize timer base: " + t);
		}
		
		myTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				doTick();
			}
		}, 900000, 900000); // 15 min
		
		BundleContext context = ctx.getBundleContext();
		tracker = new MServiceTracker<ScheduledService>(context,ScheduledService.class) {
			
			@Override
			protected void removeService(ServiceReference<ScheduledService> reference, ScheduledService service) {
				removeSchedulerService(service);
			}
			
			@Override
			protected void addService(ServiceReference<ScheduledService> reference, ScheduledService service) {
				addSchedulerService(reference, service);
			}
		}.start();
		
	}

	protected void addSchedulerService(ServiceReference<ScheduledService> reference, ScheduledService service) {
		// get interval configuration
		Object interval = service.getInterval();
		if (interval == null)
			interval = reference.getProperty("interval");
		if (interval == null) {
			log().i("interval configuration not found for SchedulerService",service,reference);
			return;
		}
		// parse configuration and create job
		String i = String.valueOf(interval);
		SchedulerJob timerTask = null;
		if (i.indexOf(' ') > 0 ) {
			timerTask = new CronJob(String.valueOf(i), service);
		} else
		if (i.indexOf(',') > 0) {
			long s = MTimeInterval.toTime(MString.beforeIndex(i,','), -1);
			long l = MTimeInterval.toTime(MString.afterIndex(i,','), -1);
			if (s > 0 && l > 0)
				timerTask = new IntervalWithStartTimeJob(s,l, service);
		} else {
			long l = MTimeInterval.toTime(i, -1);
			if (l > 0)
				timerTask = new IntervalJob(l, service);
		}
		
		if (timerTask != null) {
			services.put(service,timerTask);
			myTimer.schedule(timerTask);
		} else {
			log().i("interval configuration syntax error for SchedulerService",service,reference);
		}
		
	}

	protected void removeSchedulerService(ScheduledService service) {
		TimerTask timerTask = services.get(service);
		if (timerTask != null) {
			
		} else {
			log().i("timer task not found for SchedulerService", service);
		}
	}

	protected void doTick() {
		doCheckTimers();
	}

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
	
	private static class TimerTaskWrap extends MTimerTask implements Wrap {

		private TimerTask task;
		private Bundle bundle;
		private long modified = 0;
		private TimerWrap timer;
		private BundleContext bundleContext;
		
		public TimerTaskWrap(TimerWrap timer, TimerTask task) {
			
			if (task != null && task instanceof Named)
				setName(((Named)task).getName());
			else
				setName(MSystem.getClassName(task));
			
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
				if (!doCheck()) return;
				task.run();
			} catch (Throwable t) {
				MLogUtil.log().i("error",bundle.getBundleId(),bundle.getSymbolicName(),task.getClass().getCanonicalName(), t);
				if (task instanceof TimerTaskSelfControl) {
					if ( ((TimerTaskSelfControl)task).isCancelOnError() )
							cancel();
				} else
					cancel();
			}
		}
		
		public boolean doCheck() {
			if (bundle.getState() != Bundle.ACTIVE /* || bundle.getLastModified() != modified */ || bundleContext != bundle.getBundleContext()) {
				MLogUtil.log().i("stop timertask 2",bundle.getBundleId(),bundle.getSymbolicName(),task.getClass().getCanonicalName());
				cancel();
				return false;
			}
			return true;
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
		public TimerTask getTask() {
			return task;
		}
	}
	
	private static class SchedulerJobWrap extends SchedulerJobProxy implements Wrap {

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
		public void doTick(boolean forced) {
			
			if (!doCheck()) return;
			
			super.doTick(forced);
		}
		
		boolean doCheck() {
			if (bundle.getState() != Bundle.ACTIVE /* || bundle.getLastModified() != modified */ || bundleContext != bundle.getBundleContext()) {
				log.i("stop scheduled task 1",bundle.getBundleId(),bundle.getSymbolicName(),getTask().getClass().getCanonicalName());
				cancel();
				return false;
			}
			return true;
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

	public void doCheckTimers() {
		int cnt = 0;
		for (SchedulerJob job : myTimer.getScheduledJobs()) {
			if (job instanceof SchedulerJobWrap) {
				if (!((SchedulerJobWrap)job).doCheck()) cnt++;
			} else {
				Object task = job.getTask();
				if (task == null) {} else
				if (task instanceof de.mhus.lib.core.schedule.ObserverTimerTaskAdapter)
					task = ((de.mhus.lib.core.schedule.ObserverTimerTaskAdapter)task).getTask();
				if (task == null) {} else
				if (task instanceof TimerTaskWrap) {
					if (!((TimerTaskWrap)task).doCheck()) cnt++;
				} else {
					Bundle bundle = FrameworkUtil.getBundle(task.getClass());
					if (bundle.getState() != Bundle.ACTIVE) {
						MLogUtil.log().i("stop timertask 3",bundle.getBundleId(),bundle.getSymbolicName(),task.getClass().getCanonicalName());
						job.cancel();
						cnt++;
					}
				}
			}
		}
		MLogUtil.log().i("check common timer","removed",cnt);
	}

	public static void doDebugInfo() {
		for (SchedulerJob job : instance.myTimer.getScheduledJobs()) {
			Object task = job.getTask();
			String info = " ";
			if (task instanceof de.mhus.lib.core.schedule.ObserverTimerTaskAdapter) {
				task = ((de.mhus.lib.core.schedule.ObserverTimerTaskAdapter)task).getTask();
				info+="ObserverTimerTaskAdapter ";
			}
			if (task instanceof TimerTaskWrap) {
				task = ((TimerTaskWrap)task).getTask();
				info+="TimerTaskWrap ";
			}
			MLogUtil.log().i("JOB",job.getClass(),job.getName(),info,task == null ? "null" : task.getClass());
		}
	}
	
}