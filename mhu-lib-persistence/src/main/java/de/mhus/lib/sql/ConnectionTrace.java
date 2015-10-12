package de.mhus.lib.sql;

import java.util.Date;

import de.mhus.lib.core.MDate;
import de.mhus.lib.core.logging.Log;

public class ConnectionTrace implements Comparable<ConnectionTrace> {
	
	private StackTraceElement[] stackTrace;
	private long time;
	private long id;

	public ConnectionTrace(DbConnection con) {
		id = con.getInstanceId();
		time = System.currentTimeMillis();
		stackTrace = Thread.currentThread().getStackTrace();
	}

	@Override
	public String toString() {
		return MDate.toIsoDateTime(new Date(time));
	}

//	public StackTraceElement[] getStackTrace() {
//		return stackTrace;
//	}

	@Override
	public int compareTo(ConnectionTrace o) {
		return Long.compare(time, o.time);
	}

	public void log(Log log) {
		log.w(id,"Connection",this);
		for (StackTraceElement element : stackTrace) {
			log.w(id,"  " + element);
		}
		
	}
	
}
