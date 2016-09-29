package de.mhus.lib.core.system;

import java.lang.ref.WeakReference;
import java.util.TimerTask;

import de.mhus.lib.core.MHousekeeper;
import de.mhus.lib.core.MHousekeeperTask;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MTimer;
import de.mhus.lib.core.MTimerTask;

public class DefaultHousekeeper extends MLog implements MHousekeeper {

	private MTimer timer;

	public DefaultHousekeeper() {
		log().t("new default housekeeper");
		timer = new MTimer(true);
	}
	
	@Override
	public void register(MHousekeeperTask task, long sleep, boolean weak) {
		timer.schedule(new MyTimerTask(task,weak), sleep, sleep);
	}
	
	@Override
	public void finalize() {
		log().t("finalize");
		if (timer != null) timer.cancel();
		timer = null;
	}
	
	private class MyTimerTask extends MTimerTask {

		private WeakReference<TimerTask> refWeak;
		private TimerTask ref;

		public MyTimerTask(TimerTask task, boolean weak) {
			if (weak)
				refWeak = new WeakReference<TimerTask>(task);
			else
				ref = task;
				
		}

		@Override
		public void doit() throws Exception {
			
			TimerTask r = null;
			
			if (refWeak != null)
				r = refWeak.get();
			else
				r = ref;
			
			if (r == null) {
				this.cancel();
				return;
			}
			
			r.run();
		}
		
	}

}
