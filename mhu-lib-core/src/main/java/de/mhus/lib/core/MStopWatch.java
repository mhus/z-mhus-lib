/*
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

import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.core.jmx.MJmx;
import de.mhus.lib.core.service.UniqueId;

/**
 * <p>MStopWatch class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@JmxManaged(descrition="Simple Stop Watch")
public class MStopWatch extends MJmx {

	private static final int STATUS_INITIAL = 0;
	private static final int STATUS_RUNNING = 1;
	private static final int STATUS_STOPPED = 2;

	private long start = 0;
	private long stop = 0;

	private String name;
	
	/**
	 * <p>Constructor for MStopWatch.</p>
	 */
	public MStopWatch() {
		name = "StopWatch " + MSingleton.baseLookup(this,UniqueId.class).nextUniqueId();
	}
	
	/**
	 * <p>Constructor for MStopWatch.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public MStopWatch(String name) {
		this.name = name;
	}
	
	/**
	 * <p>start.</p>
	 *
	 * @return a {@link de.mhus.lib.core.MStopWatch} object.
	 */
	public MStopWatch start() {
		if (start == 0)
			start = System.currentTimeMillis();
		return this;
	}

	/**
	 * <p>stop.</p>
	 *
	 * @return a {@link de.mhus.lib.core.MStopWatch} object.
	 */
	public MStopWatch stop() {
		if (start != 0 && stop == 0) {
			stop = System.currentTimeMillis();
			
//			try {
//				if (MSingleton.instance().isPersistence()) {
//					IConfig persistence = MSingleton.instance().getPersistenceManager().sessionScope().getPersistence("de.mhus.lib");
//					long uid = MSingleton.instance().nextUniqueId();
//					persistence.setString(getJmxName() + "_" + name + "_" + uid, getCurrentTimeAsString());
//					persistence.save();
//				}
//			} catch (Throwable t) {
//				log().t(t);
//			}
			
		}
		return this;
	}

	/**
	 * <p>getCurrentTime.</p>
	 *
	 * @return a long.
	 */
	public long getCurrentTime() {
		if (start == 0)
			return 0;
		if (stop == 0)
			return System.currentTimeMillis() - start;
		return stop - start;
	}

	/**
	 * <p>reset.</p>
	 *
	 * @return a {@link de.mhus.lib.core.MStopWatch} object.
	 */
	public MStopWatch reset() {
		start = 0;
		stop = 0;
		return this;
	}

	/**
	 * <p>getStatus.</p>
	 *
	 * @return a int.
	 */
	public int getStatus() {
		if (start == 0 && stop == 0)
			return STATUS_INITIAL;
		if (stop == 0)
			return STATUS_RUNNING;
		return STATUS_STOPPED;
	}
	
	/**
	 * <p>isRunning.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isRunning() {
		return getStatus() == STATUS_RUNNING;
	}

	/**
	 * <p>getStatusAsString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@JmxManaged(descrition="Current status of the watch")
	public String getStatusAsString() {
		switch (getStatus()) {
		case STATUS_INITIAL: return "initial";
		case STATUS_RUNNING: return "running";
		case STATUS_STOPPED: return "stopped";
		default: return "unknown";
		}
	}
	
	/**
	 * <p>getCurrentSeconds.</p>
	 *
	 * @return a long.
	 */
	public long getCurrentSeconds() {
		return getCurrentTime() / 1000;
	}

	/**
	 * <p>getCurrentMinutes.</p>
	 *
	 * @return a long.
	 */
	public long getCurrentMinutes() {
		return getCurrentSeconds() / 60;
	}

	/**
	 * <p>getCurrentMinutesAsString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCurrentMinutesAsString() {
		long sec = getCurrentSeconds();
		return String.valueOf(sec / 60) + ':'
				+ MCast.toString((int) (sec % 60), 2);
	}

	/**
	 * <p>getCurrentTimeAsString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@JmxManaged(descrition="Currently elapsed time")
	public String getCurrentTimeAsString() {
		return MTimeInterval.getIntervalAsString(getCurrentTime());
	}

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@JmxManaged(descrition="Name of the watch")
	public String getName() {
		return name;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return name + "=" + getCurrentTimeAsString();
	}
	
}
