package de.mhus.lib.core;

import java.util.Vector;

import de.mhus.lib.core.MThread.ThreadContainer;
import de.mhus.lib.core.lang.IBase;
import de.mhus.lib.core.lang.MObject;

/**
 * <p>MThreadManager class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MThreadManager extends MObject implements IBase {

	/** Constant <code>SLEEP_TIME=1000 * 60 * 10</code> */
	public static long SLEEP_TIME = 1000 * 60 * 10;
	/** Constant <code>PENDING_TIME=1000 * 60</code> */
	public static long PENDING_TIME = 1000 * 60;
	private Vector<ThreadContainer> pool = new Vector<ThreadContainer>();
	private ThreadGroup group = new ThreadGroup("AThread");
	private ThreadHousekeeper housekeeper;

	private class ThreadHousekeeper extends MHousekeeperTask {

		@Override
		public void doit() {
			log().t(getClass(),"Housekeeper");
			poolClean(PENDING_TIME);
			MThreadDaemon.poolClean(PENDING_TIME);
		}
	}


	ThreadContainer start(MThread _task, String _name) {

		ThreadContainer tc = null;
		synchronized (pool) {
			if (housekeeper == null) {
				housekeeper = new ThreadHousekeeper();
				base(MHousekeeper.class).register(housekeeper, SLEEP_TIME, true);
			}
			// search free thread

			for (int i = 0; i < pool.size(); i++)
				if (!pool.elementAt(i).isWorking()) {
					tc = pool.elementAt(i);
					break;
				}

			if (tc == null) {
				tc = new ThreadContainer(group, "AT" + pool.size());
				tc.start();
				pool.addElement(tc);
			}

			log().t("###: NEW THREAD",tc.getId());
			tc.setName(_name);
			tc.newWork(_task);
		}

		return tc;
	}

	/**
	 * <p>poolClean.</p>
	 *
	 * @param pendingTime a long.
	 */
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

	/**
	 * <p>poolClean.</p>
	 */
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

	/**
	 * <p>poolSize.</p>
	 *
	 * @return a int.
	 */
	public int poolSize() {
		synchronized (pool) {
			return pool.size();
		}

	}

	/**
	 * <p>poolWorkingSize.</p>
	 *
	 * @return a int.
	 */
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

	/** {@inheritDoc} */
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
