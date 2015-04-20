package de.mhus.lib.core;

import java.lang.ref.WeakReference;
import java.util.TimerTask;

import de.mhus.lib.core.lang.IBase;

public class MHousekeeper extends MLog implements IBase {
	
	private MTimer timer;
	
	public MHousekeeper() {
		log().t("new housekeeper");
		timer = new MTimer(true);
	}
	
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
		public void doIt() throws Exception {
			
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
