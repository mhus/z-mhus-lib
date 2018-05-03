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
	public void register(MHousekeeperTask task, long sleep) {
		timer.schedule(new MyTimerTask(task), sleep, sleep);
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

		public MyTimerTask(TimerTask task) {
			refWeak = new WeakReference<TimerTask>(task);
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
			if (r instanceof MTimerTask) {
				if ( ((MTimerTask)r).isCanceled() ) {
					this.cancel();
					return;
				}

			}
				
			r.run();
		}
		
	}

}
