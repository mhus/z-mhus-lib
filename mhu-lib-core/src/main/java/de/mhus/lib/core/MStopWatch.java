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

@JmxManaged(descrition="Simple Stop Watch")
public class MStopWatch extends MJmx {

	private static final int STATUS_INITIAL = 0;
	private static final int STATUS_RUNNING = 1;
	private static final int STATUS_STOPPED = 2;

	private long start = 0;
	private long stop = 0;

	private String name;
	
	public MStopWatch() {
		name = "StopWatch " + base(UniqueId.class).nextUniqueId();
	}
	
	public MStopWatch(String name) {
		this.name = name;
	}
	
	public MStopWatch start() {
		if (start == 0)
			start = System.currentTimeMillis();
		return this;
	}

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

	public long getCurrentTime() {
		if (start == 0)
			return 0;
		if (stop == 0)
			return System.currentTimeMillis() - start;
		return stop - start;
	}

	public MStopWatch reset() {
		start = 0;
		stop = 0;
		return this;
	}

	public int getStatus() {
		if (start == 0 && stop == 0)
			return STATUS_INITIAL;
		if (stop == 0)
			return STATUS_RUNNING;
		return STATUS_STOPPED;
	}
	
	public boolean isRunning() {
		return getStatus() == STATUS_RUNNING;
	}

	@JmxManaged(descrition="Current status of the watch")
	public String getStatusAsString() {
		switch (getStatus()) {
		case STATUS_INITIAL: return "initial";
		case STATUS_RUNNING: return "running";
		case STATUS_STOPPED: return "stopped";
		default: return "unknown";
		}
	}
	
	public long getCurrentSeconds() {
		return getCurrentTime() / 1000;
	}

	public long getCurrentMinutes() {
		return getCurrentSeconds() / 60;
	}

	public String getCurrentMinutesAsString() {
		long sec = getCurrentSeconds();
		return String.valueOf(sec / 60) + ':'
				+ MCast.toString((int) (sec % 60), 2);
	}

	@JmxManaged(descrition="Currently elapsed time")
	public String getCurrentTimeAsString() {
		return getCurrentTimeAsString(true);
	}
	
	public String getCurrentTimeAsString(boolean longFormat) {
		long msec = getCurrentTime();
		long sec = msec / 1000;
		long min = sec / 60;
		if (!longFormat && min < 60)
			return MCast.toString((int) (min), 2) + ':'
					+ MCast.toString((int) (sec % 60), 2) + '.'
					+ MCast.toString((int) (msec % 1000), 3);
		long hours = min / 60;
		if (!longFormat && hours < 24)
			return MCast.toString((int) (hours), 2) + ':'
					+ MCast.toString((int) (min % 60), 2) + ':'
					+ MCast.toString((int) (sec % 60), 2) + '.'
					+ MCast.toString((int) (msec % 1000), 3);
		long days = hours / 24;
		return MCast.toString((int) (days), 2) + ' '
				+ MCast.toString((int) (hours % 24), 2) + ':'
				+ MCast.toString((int) (min % 60), 2) + ':'
				+ MCast.toString((int) (sec % 60), 2) + '.'
				+ MCast.toString((int) (msec % 1000), 3);

	}

	@JmxManaged(descrition="Name of the watch")
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name + "=" + getCurrentTimeAsString() + "@MStopWatch";
	}
	
}
