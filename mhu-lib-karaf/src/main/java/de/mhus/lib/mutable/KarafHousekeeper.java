package de.mhus.lib.mutable;

import java.lang.ref.WeakReference;
import java.util.Observable;
import java.util.Observer;

import de.mhus.lib.core.MHousekeeper;
import de.mhus.lib.core.MHousekeeperTask;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.schedule.IntervalJob;
import de.mhus.lib.core.util.TimerIfc;

public class KarafHousekeeper extends MObject implements MHousekeeper {

	@Override
	public void register(MHousekeeperTask task, long sleep, boolean weak) {
		log().d("register",task,sleep,weak);
		TimerIfc timer = MSingleton.baseLookup(this,TimerIfc.class);
		if (weak) {
			WeakObserver t = new WeakObserver(task);
			IntervalJob job = new IntervalJob(sleep, t);
			t.setJob(job);
			timer.schedule(job);
		} else
			timer.schedule(new IntervalJob(sleep, task));
	}

	private static class WeakObserver implements Observer {

		private WeakReference<MHousekeeperTask> task;
		private IntervalJob job;

		public WeakObserver(MHousekeeperTask task) {
			this.task = new WeakReference<MHousekeeperTask>(task);
		}

		public void setJob(IntervalJob job) {
			this.job = job;
		}

		@Override
		public void update(Observable o, Object arg) {
			MHousekeeperTask t = task.get();
			if (t == null)
				job.cancel();
			else
				t.update(o, arg);
		}
		
		@Override
		public String toString() {
			MHousekeeperTask t = task.get();
			if (t == null)
				return super.toString();
			else
				return t.toString();
		}
		
	}
}
