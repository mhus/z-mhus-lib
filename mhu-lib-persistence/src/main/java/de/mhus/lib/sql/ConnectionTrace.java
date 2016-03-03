package de.mhus.lib.sql;

import java.util.Date;

import de.mhus.lib.core.MDate;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.MLogUtil;

/**
 * <p>ConnectionTrace class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class ConnectionTrace implements Comparable<ConnectionTrace> {
	
	private StackTraceElement[] stackTrace;
	private long time;
	private long id;

	/**
	 * <p>Constructor for ConnectionTrace.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 */
	public ConnectionTrace(DbConnection con) {
		id = con.getInstanceId();
		time = System.currentTimeMillis();
		stackTrace = Thread.currentThread().getStackTrace();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return MDate.toIsoDateTime(new Date(time));
	}

//	public StackTraceElement[] getStackTrace() {
//		return stackTrace;
//	}

	/** {@inheritDoc} */
	@Override
	public int compareTo(ConnectionTrace o) {
		return Long.compare(time, o.time);
	}

	/**
	 * <p>log.</p>
	 *
	 * @param log a {@link de.mhus.lib.core.logging.Log} object.
	 */
	public void log(Log log) {
		log.w(id,"Connection",this);
		MLogUtil.logStackTrace(log, ""+id, stackTrace);
	}
	
}
