package de.mhus.lib.core.util;

import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import de.mhus.lib.core.logging.Log;

public class TimerImpl implements TimerIfc {
	
	private Timer timer;
	private LinkedList<TimerTaskWrap> tasks = new LinkedList<>();
	private Log log = Log.getLog(TimerImpl.class);
	
	public TimerImpl(Timer timer) {
		this.timer = timer;
	}
	
	@Override
	public int hashCode() {
		return timer.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return timer.equals(obj);
	}

	@Override
	public void schedule(TimerTask task, long delay) {
		timer.schedule(new TimerTaskWrap(this, task), delay);
	}

	@Override
	public void schedule(TimerTask task, Date time) {
		timer.schedule(new TimerTaskWrap(this, task), time);
	}

	@Override
	public void schedule(TimerTask task, long delay, long period) {
		timer.schedule(new TimerTaskWrap(this, task), delay, period);
	}

	@Override
	public String toString() {
		return timer.toString();
	}

	@Override
	public void schedule(TimerTask task, Date firstTime, long period) {
		timer.schedule(new TimerTaskWrap(this, task), firstTime, period);
	}

	@Override
	public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
		timer.scheduleAtFixedRate(new TimerTaskWrap(this, task), delay, period);
	}

	@Override
	public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
		timer.scheduleAtFixedRate(new TimerTaskWrap(this, task), firstTime, period);
	}

	@Override
	public void cancel() {
		synchronized (this) {
			for (TimerTaskWrap task : tasks)
				task.cancelDirect();
		}
	}

	public int purge() {
		return timer.purge();
	}

	private class TimerTaskWrap extends TimerTask {

		private TimerTask task;
		private TimerImpl timer;
		
		public TimerTaskWrap(TimerImpl timer, TimerTask task) {
			this.task = task;
			this.timer = timer;
			synchronized (timer) {
				timer.tasks.add(this);
			}
		}
		@Override
		public void run() {
			try {
//				if (task.isCancelled(task)) {
//					cancel();
//					return;
//				}
				task.run();
			} catch (Throwable t) {
				log.i("error",task.getClass().getCanonicalName(), t);
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

}
