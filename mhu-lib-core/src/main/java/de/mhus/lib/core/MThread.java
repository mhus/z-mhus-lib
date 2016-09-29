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

import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.logging.Log;


/**
 * <p>MThread class.</p>
 *
 * @author hummel
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 * @version $Id: $Id
 */
public class MThread extends MObject implements Runnable {

	/** Constant <code>log</code> */
	protected static Log log = Log.getLog(MThread.class);
	
	protected Runnable task = this;
	protected String name = "";
	protected ThreadContainer tc = null;

	/**
	 * <p>Constructor for MThread.</p>
	 */
	public MThread() {
	}

	/**
	 * <p>Constructor for MThread.</p>
	 *
	 * @param _name a {@link java.lang.String} object.
	 */
	public MThread(String _name) {
		name = _name;
	}

	/**
	 * <p>Constructor for MThread.</p>
	 *
	 * @param _task a {@link java.lang.Runnable} object.
	 */
	public MThread(Runnable _task) {
		task = _task;
	}

	/**
	 * <p>Constructor for MThread.</p>
	 *
	 * @param _task a {@link java.lang.Runnable} object.
	 * @param _name a {@link java.lang.String} object.
	 */
	public MThread(Runnable _task, String _name) {
		task = _task;
		name = _name;
	}

	/**
	 * <p>Getter for the field <code>task</code>.</p>
	 *
	 * @return a {@link java.lang.Runnable} object.
	 */
	protected Runnable getTask() {
		return task;
	}

	/** {@inheritDoc} */
	@Override
	public void run() {
	}

	/**
	 * <p>start.</p>
	 *
	 * @return a {@link de.mhus.lib.core.MThread} object.
	 */
	public MThread start() {
		tc = MSingleton.baseLookup(this,MThreadManager.class).start(this, name);
		return this;
	}

	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param _name a {@link java.lang.String} object.
	 */
	public void setName(String _name) {
		if (tc != null)
			tc.setName(_name);
	}

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		if (tc != null)
			return tc.getName();
		return "";
	}

	/**
	 * <p>setPriority.</p>
	 *
	 * @param _p a int.
	 */
	public void setPriority(int _p) {
		if (tc != null)
			tc.setPriority(_p);
	}

	/**
	 * <p>getPriority.</p>
	 *
	 * @return a int.
	 */
	public int getPriority() {
		if (tc != null)
			return tc.getPriority();
		return 0;
	}

	/**
	 * <p>stop.</p>
	 */
	@SuppressWarnings("deprecation")
	public void stop() {
		if (tc == null)
			return;
		tc.stop();
	}

	/**
	 * <p>throwException.</p>
	 *
	 * @param throwable a {@link java.lang.Throwable} object.
	 */
	@SuppressWarnings("deprecation")
	public void throwException(Throwable throwable) {
		if (tc == null)
			return;
		tc.stop(throwable);
	}
	
	/**
	 * Sleeps _millisec milliseconds. On Error (e.g. a break), it prints a
	 * stacktrace dump.
	 *
	 * @param _millisec a long.
	 */
	public static void sleep(long _millisec) {
		try {
			Thread.sleep(_millisec);
		} catch (InterruptedException e) {
			log.i(e);
		}
	}

	private void taskFinish() {
		tc = null;
	}

	/**
	 * <p>taskError.</p>
	 *
	 * @param t a {@link java.lang.Throwable} object.
	 */
	public void taskError(Throwable t) {

	}

	protected static class ThreadContainer extends Thread {

		private boolean running = true;
		private MThread task = null;
		private String name;
		private long sleepStart;

		public ThreadContainer(ThreadGroup group, String pName) {
			super(group, pName);
			name = pName;
			setName(name + " sleeping");
		}

		public synchronized boolean newWork(MThread _task) {
			synchronized (this) {
				if (task != null || !running)
					return false;
				task = _task;
				notify();
			}
			return true;
		}

		public boolean isWorking() {
			synchronized (this) {
				return task != null;
			}
		}

		public boolean isRunning() {
			return running;
		}

		public boolean stopRunning() {
			synchronized (this) {
				if (task != null)
					return false;
				running = false;
				notifyAll();
			}
			return true;
		}

		public long getSleepTime() {
			if (task != null)
				return 0;
			return System.currentTimeMillis() - sleepStart;
		}

		@Override
		public void run() {

			while (running) {

				sleepStart = System.currentTimeMillis();
				while (task == null && running) {
					// AThread.sleep( 100 );
					try {
						synchronized (this) {
							this.wait();
						}
					} catch (InterruptedException e) {
					}
				}

				MThread currentTask = task;
				if (task != null) {

					// run ....
					setName(name + '[' + getId() + "] "
							+ currentTask.getTask().getClass().getName());
					try {
						log.d("Enter Thread Task");
						currentTask.getTask().run();
						log.d("Leave Thread Task");
					} catch (Throwable t) {
						try {
							log.i("Thread Task Error", getName(), t);
							currentTask.taskError(t);
						} catch (Throwable t2) {
							log.i("Thread Task Finish Error", getName(), t2);
						}
					}
					log.d("###: LEAVE THREAD");
					setName(name + " sleeping");
				}
				if (currentTask != null)
					currentTask.taskFinish();
				task = null; // don't need sync

			}
		}

	}

	/**
	 * <p>asynchron.</p>
	 *
	 * @param task a {@link java.lang.Runnable} object.
	 * @since 3.3.0
	 */
	public static void asynchron(Runnable task) {
		new MThread(task).start();
	}

}
