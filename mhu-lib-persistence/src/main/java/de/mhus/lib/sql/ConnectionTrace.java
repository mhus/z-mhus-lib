package de.mhus.lib.sql;

import java.util.Arrays;
import java.util.Date;

import de.mhus.lib.core.MDate;

public class ConnectionTrace {
	
	private StackTraceElement[] stackTrace;
	private long time;

	public ConnectionTrace() {
		time = System.currentTimeMillis();
		stackTrace = Thread.currentThread().getStackTrace();
	}

	public String toString() {
		return MDate.toIsoDateTime(new Date(time)) + " " + Arrays.toString(stackTrace);
	}
	
}
