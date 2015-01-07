/*
 * ./core/de/mhu/lib/AThread.java
 *  Copyright (C) 2002-2004 Mike Hummel
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package de.mhus.lib.core;

import java.util.Vector;

/**
 * @author hummel
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MThreadDaemon extends MThread implements Runnable {

	public MThreadDaemon() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MThreadDaemon(Runnable _task, String _name) {
		super(_task, _name);
		// TODO Auto-generated constructor stub
	}

	public MThreadDaemon(Runnable _task) {
		super(_task);
		// TODO Auto-generated constructor stub
	}

	public MThreadDaemon(String _name) {
		super(_name);
		// TODO Auto-generated constructor stub
	}

	private static Vector<ThreadContainer> pool = new Vector<ThreadContainer>();
	private static ThreadGroup group = new ThreadGroup("AThreadDeamon");

	@Override
	public void start() {
		tc = start(this, name);
	}

	private static ThreadContainer start(MThreadDaemon _task, String _name) {

		// search free thread
		ThreadContainer tc = null;
		synchronized (pool) {

			for (int i = 0; i < pool.size(); i++)
				if (!pool.elementAt(i).isWorking()) {
					tc = pool.elementAt(i);
					break;
				}

			if (tc == null) {
				tc = new ThreadContainer(group, "AT_" + pool.size());
				tc.setDaemon(true);
				tc.start();
				pool.addElement(tc);
			}

			log.t("###: NEW THREAD",tc.getId());
			tc.setName(_name);
			tc.newWork(_task);
		}
		return tc;
	}

	public static void poolClean(long pendingTime) {
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

	public static void poolClean() {

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

	public static int poolSize() {
		synchronized (pool) {
			return pool.size();
		}

	}

	public static int poolWorkingSize() {
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

}
