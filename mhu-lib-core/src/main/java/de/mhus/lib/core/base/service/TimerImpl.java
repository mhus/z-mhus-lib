/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.base.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.TrailLevelMapper;
import de.mhus.lib.core.schedule.SchedulerJob;
import de.mhus.lib.core.util.TimerTaskSelfControl;

public class TimerImpl extends MLog implements TimerIfc {
	
	private Timer timer;
	private LinkedList<TimerTaskWrap> tasks = new LinkedList<>();
	
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
		log().d("schedule",task,delay);
		timer.schedule(new TimerTaskWrap(this, null, task), delay);
	}

	@Override
	public void schedule(TimerTask task, Date time) {
		log().d("schedule",task,time);
		timer.schedule(new TimerTaskWrap(this, null, task), time);
	}

	@Override
	public void schedule(TimerTask task, long delay, long period) {
		log().d("schedule",task,delay,period);
		timer.schedule(new TimerTaskWrap(this, null, task), delay, period);
	}

	@Override
	public void schedule(TimerTask task, Date firstTime, long period) {
		log().d("schedule",task,firstTime,period);
		timer.schedule(new TimerTaskWrap(this, null, task), firstTime, period);
	}

	@Override
	public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
		log().d("scheduleAtFixedRate",task,delay,period);
		timer.scheduleAtFixedRate(new TimerTaskWrap(this, null, task), delay, period);
	}

	@Override
	public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
		log().d("scheduleAtFixedRate",task,firstTime,period);
		timer.scheduleAtFixedRate(new TimerTaskWrap(this, null, task), firstTime, period);
	}

	@Override
	public void schedule(String name, TimerTask task, long delay) {
		log().d("schedule",name,task,delay);
		timer.schedule(new TimerTaskWrap(this,name, task), delay);
	}

	@Override
	public void schedule(String name, TimerTask task, Date time) {
		log().d("schedule",name,task,time);
		timer.schedule(new TimerTaskWrap(this,name, task), time);
	}

	@Override
	public void schedule(String name, TimerTask task, long delay, long period) {
		log().d("schedule",name,task,delay,period);
		timer.schedule(new TimerTaskWrap(this,name, task), delay, period);
	}

	@Override
	public void schedule(String name, TimerTask task, Date firstTime, long period) {
		log().d("schedule",name,task,firstTime,period);
		timer.schedule(new TimerTaskWrap(this,name, task), firstTime, period);
	}

	@Override
	public void scheduleAtFixedRate(String name, TimerTask task, long delay, long period) {
		log().d("scheduleAtFixedRate",name,task,delay,period);
		timer.scheduleAtFixedRate(new TimerTaskWrap(this,name, task), delay, period);
	}

	@Override
	public void scheduleAtFixedRate(String name, TimerTask task, Date firstTime, long period) {
		log().d("scheduleAtFixedRate",name,task,firstTime,period);
		timer.scheduleAtFixedRate(new TimerTaskWrap(this,name, task), firstTime, period);
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
		private String log;
		private String name;
		
		public TimerTaskWrap(TimerImpl timer,String name, TimerTask task) {
			this.task = task;
			this.timer = timer;
			if (name == null) name = MSystem.getClassName(task);
			this.name = name;
			synchronized (timer) {
				timer.tasks.add(this);
			}
			LevelMapper lm = MApi.get().getLogFactory().getLevelMapper();
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
					LevelMapper lm = MApi.get().getLogFactory().getLevelMapper();
					if (lm != null && lm instanceof TrailLevelMapper)
						((TrailLevelMapper)lm).doConfigureTrail(null, log);
				}
				
				log().t("run",name,task);
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
					LevelMapper lm = MApi.get().getLogFactory().getLevelMapper();
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

	@Override
	public void schedule(SchedulerJob scheduler) {
		schedule(scheduler, 1000, 1000);
	}

	@Override
	public String toString() {
		return timer.toString();
	}


}
