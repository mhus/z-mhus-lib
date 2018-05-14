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
package de.mhus.lib.core;

import java.util.Vector;

import de.mhus.lib.core.MThread.ThreadContainer;
import de.mhus.lib.core.lang.IBase;

/**
 * Original thread pool manager using a real thread pool.
 * 
 * @author mikehummel
 *
 */
public class MThreadPoolManager extends MThreadManager implements IBase {

	public static long SLEEP_TIME = 1000 * 60 * 10;
	public static long PENDING_TIME = 1000 * 60;
	private Vector<ThreadContainer> pool = new Vector<ThreadContainer>();
	private ThreadGroup group = new ThreadGroup("AThread");
	private ThreadHousekeeper housekeeper;

	private class ThreadHousekeeper extends MHousekeeperTask {

		@Override
		public void doit() {
			log().t(getClass(),"Housekeeper");
			poolClean(PENDING_TIME);
			try {
				MThreadDaemon.poolClean(PENDING_TIME);
			} catch (NoClassDefFoundError e) {
				// this only happens in OSGi if the bundle was uninstalled
				log().i("Close stale ThreadHousekeeper", e.toString() );
				cancel();
			}
		}
	}


	@Override
	ThreadContainer start(MThread _task, String _name) {

		ThreadContainer tc = null;
		synchronized (pool) {
			if (housekeeper == null) {
				housekeeper = new ThreadHousekeeper();
				MApi.lookup(MHousekeeper.class).register(housekeeper, SLEEP_TIME);
			}
			// search free thread

			for (int i = 0; i < pool.size(); i++)
				if (!pool.elementAt(i).isWorking()) {
					tc = pool.elementAt(i);
					break;
				}

			if (tc == null) {
				tc = new ThreadContainer(group, "AT" + pool.size(), false);
				tc.start();
				pool.addElement(tc);
			}

			log().t("###: NEW THREAD",tc.getId());
			tc.setName(_name);
			tc.newWork(_task);
		}

		return tc;
	}

	@Override
	public void poolClean(long pendingTime) {
		synchronized (pool) {
			ThreadContainer[] list = pool
					.toArray(new ThreadContainer[pool.size()]);
			for (int i = 0; i < list.length; i++) {
				long sleep = list[i].getSleepTime();
				if (sleep != 0 && sleep <= pendingTime) {
					pool.remove(list[i]);
					list[i].stopRunning();
				}
			}
		}
	}

	@Override
	public void poolClean() {

		synchronized (pool) {
			ThreadContainer[] list = pool
					.toArray(new ThreadContainer[pool.size()]);
			for (int i = 0; i < list.length; i++) {
				if (!list[i].isWorking()) {
					pool.remove(list[i]);
					list[i].stopRunning();
				}
			}
		}
	}

	@Override
	public int poolSize() {
		synchronized (pool) {
			return pool.size();
		}

	}

	@Override
	public int poolWorkingSize() {
		int size = 0;
		synchronized (pool) {
			ThreadContainer[] list = pool
					.toArray(new ThreadContainer[pool.size()]);
			for (int i = 0; i < list.length; i++) {
				if (list[i].isWorking())
					size++;
			}
		}
		return size;
	}

	@Override
	protected void finalize() {
		log().t("finalize");
		synchronized (pool) {
			ThreadContainer[] list = pool
					.toArray(new ThreadContainer[pool.size()]);
			for (ThreadContainer tc : list) {
				tc.stopRunning();
			}
		}
	}
	
}
