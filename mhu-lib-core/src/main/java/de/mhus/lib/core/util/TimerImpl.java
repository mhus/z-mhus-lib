package de.mhus.lib.core.util;

import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.TrailLevelMapper;
import de.mhus.lib.core.schedule.SchedulerJob;

/**
 * <p>TimerImpl class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class TimerImpl extends MLog implements TimerIfc {
	
	private Timer timer;
	private LinkedList<TimerTaskWrap> tasks = new LinkedList<>();
	
	/**
	 * <p>Constructor for TimerImpl.</p>
	 *
	 * @param timer a {@link java.util.Timer} object.
	 */
	public TimerImpl(Timer timer) {
		this.timer = timer;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return timer.hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		return timer.equals(obj);
	}

	/** {@inheritDoc} */
	@Override
	public void schedule(TimerTask task, long delay) {
		log().d("schedule",task,delay);
		timer.schedule(new TimerTaskWrap(this, task), delay);
	}

	/** {@inheritDoc} */
	@Override
	public void schedule(TimerTask task, Date time) {
		log().d("schedule",task,time);
		timer.schedule(new TimerTaskWrap(this, task), time);
	}

	/** {@inheritDoc} */
	@Override
	public void schedule(TimerTask task, long delay, long period) {
		log().d("schedule",task,delay,period);
		timer.schedule(new TimerTaskWrap(this, task), delay, period);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return timer.toString();
	}

	/** {@inheritDoc} */
	@Override
	public void schedule(TimerTask task, Date firstTime, long period) {
		log().d("schedule",task,firstTime,period);
		timer.schedule(new TimerTaskWrap(this, task), firstTime, period);
	}

	/** {@inheritDoc} */
	@Override
	public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
		log().d("scheduleAtFixedRate",task,delay,period);
		timer.scheduleAtFixedRate(new TimerTaskWrap(this, task), delay, period);
	}

	/** {@inheritDoc} */
	@Override
	public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
		log().d("scheduleAtFixedRate",task,firstTime,period);
		timer.scheduleAtFixedRate(new TimerTaskWrap(this, task), firstTime, period);
	}

	/** {@inheritDoc} */
	@Override
	public void cancel() {
		synchronized (this) {
			for (TimerTaskWrap task : tasks)
				task.cancelDirect();
		}
	}

	/**
	 * <p>purge.</p>
	 *
	 * @return a int.
	 */
	public int purge() {
		return timer.purge();
	}

	private class TimerTaskWrap extends TimerTask {

		private TimerTask task;
		private TimerImpl timer;
		private String log;
		
		public TimerTaskWrap(TimerImpl timer, TimerTask task) {
			this.task = task;
			this.timer = timer;
			synchronized (timer) {
				timer.tasks.add(this);
			}
			LevelMapper lm = MSingleton.get().getLogFactory().getLevelMapper();
			if (lm != null && lm instanceof TrailLevelMapper)
				log = ((TrailLevelMapper)lm).doSerializeTrail();
		}
		@Override
		public void run() {
			try {
//				if (task.isCancelled(task)) {
//					cancel();
//					return;
//				}
				if (log != null) {
					LevelMapper lm = MSingleton.get().getLogFactory().getLevelMapper();
					if (lm != null && lm instanceof TrailLevelMapper)
						((TrailLevelMapper)lm).doConfigureTrail(log);
				}
				
				log().t("run",task);
				task.run();
			} catch (Throwable t) {
				log().i("error",task.getClass().getCanonicalName(), t);
				if (task instanceof TimerTaskSelfControl) {
					if ( ((TimerTaskSelfControl)task).isCancelOnError() )
							cancel();
				} else
					cancel();
			} finally {
				if (log != null) {
					LevelMapper lm = MSingleton.get().getLogFactory().getLevelMapper();
					if (lm != null && lm instanceof TrailLevelMapper)
						((TrailLevelMapper)lm).doResetTrail();
				}
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

	/** {@inheritDoc} */
	@Override
	public void schedule(SchedulerJob scheduler) {
		schedule(scheduler, 1000, 1000);
	}

}
