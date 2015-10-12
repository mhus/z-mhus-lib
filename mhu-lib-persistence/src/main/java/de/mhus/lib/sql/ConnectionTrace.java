package de.mhus.lib.sql;

import java.util.Date;

import de.mhus.lib.core.MDate;

public class ConnectionTrace implements Comparable<ConnectionTrace> {
	
	private StackTraceElement[] stackTrace;
	private long time;

	public ConnectionTrace() {
		time = System.currentTimeMillis();
		stackTrace = Thread.currentThread().getStackTrace();
	}

	@Override
	public String toString() {
		return MDate.toIsoDateTime(new Date(time));
	}

	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}

	@Override
	public int compareTo(ConnectionTrace o) {
		return Long.compare(time, o.time);
	}
	
}
