package de.mhus.lib.logging.level;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MMath;
import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.Log.LEVEL;

/**
 * <p>ThreadMapperConfig class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class ThreadMapperConfig implements LevelMapper {

	/** Constant <code>MAP_LABEL="MAP"</code> */
	public static final String MAP_LABEL = "MAP";
	private static long nextId = 0;

	private LEVEL debug = LEVEL.INFO;
	private LEVEL error = LEVEL.ERROR;
	private LEVEL fatal = LEVEL.FATAL;
	private LEVEL info = LEVEL.INFO;
	private LEVEL trace = LEVEL.TRACE;
	private LEVEL warn = LEVEL.WARN;
	private boolean local = false;
	private long timeout = 0;
	private long timetout = 0;

	private String id = MMath.toBasis36WithIdent( (long) (Math.random() * 36 * 36 * 36 * 36), ++nextId, 8 );

	/**
	 * <p>isTimedOut.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isTimedOut() {
		if (timetout <= 0)
			return false;
		return System.currentTimeMillis() >= timetout;
	}
	
	/** {@inheritDoc} */
	@Override
	public LEVEL map(Log log, LEVEL level, Object... msg) {
		switch (level) {
		case DEBUG:
			return debug;
		case ERROR:
			return error;
		case FATAL:
			return fatal;
		case INFO:
			return info;
		case TRACE:
			return trace;
		case WARN:
			return warn;
		}
		return level;
	}

	/**
	 * <p>doConfigure.</p>
	 *
	 * @param config a {@link java.lang.String} object.
	 */
	public void doConfigure(String config) {
		if (config == null) return;
		String[] parts = config.toUpperCase().split(",");
		if (parts.length > 1)
			trace = toLevel(parts[1]);
		if (parts.length > 2)
			debug = toLevel(parts[2]);
		if (parts.length > 3)
			info  = toLevel(parts[3]);
		if (parts.length > 4)
			warn  = toLevel(parts[4]);
		if (parts.length > 5)
			error = toLevel(parts[5]);
		if (parts.length > 6)
			fatal = toLevel(parts[6]);
		if (parts.length > 7)
			local = parts[7].equals("L");
		if (parts.length > 8)
			setTimeout( MCast.tolong(parts[8], 0));
		if (parts.length > 9)
			id = parts[9];
	}

	private LEVEL toLevel(String in) {
		switch (in) {
		case "T":return LEVEL.TRACE;
		case "D":return LEVEL.DEBUG;
		case "I":return LEVEL.INFO;
		case "W":return LEVEL.WARN;
		case "E":return LEVEL.ERROR;
		case "F":return LEVEL.FATAL;
		}
		return LEVEL.TRACE;
	}

	/**
	 * <p>doSerialize.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String doSerialize() {
		return MAP_LABEL + "," + 
				trace.name().substring(0, 1) + "," + 
				debug.name().substring(0, 1) + "," + 
				info.name().substring(0, 1) + "," + 
				warn.name().substring(0, 1) + "," + 
				error.name().substring(0, 1) + "," + 
				fatal.name().substring(0, 1) + "," +
				(local ? "L" : "G") + "," +
				timeout + "," +
				id;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return doSerialize();
	}

	/**
	 * <p>isLocal.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isLocal() {
		return local;
	}

	/**
	 * <p>Setter for the field <code>local</code>.</p>
	 *
	 * @param local a boolean.
	 */
	public void setLocal(boolean local) {
		this.local = local;
	}

	/**
	 * <p>Getter for the field <code>timeout</code>.</p>
	 *
	 * @return a long.
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * <p>Setter for the field <code>timeout</code>.</p>
	 *
	 * @param timeout a long.
	 */
	public void setTimeout(long timeout) {
		if (timeout <= 0) {
			timetout = 0;
		} else {
			timetout = System.currentTimeMillis() + timeout;
		}
		this.timeout = timeout;
	}

	/** {@inheritDoc} */
	@Override
	public void prepareMessage(Log log, StringBuffer msg) {
		msg.append('{').append(id).append('}');
		msg.append('(').append(Thread.currentThread().getId()).append(')');
	}

	/**
	 * <p>getTrailId.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTrailId() {
		return id;
	}

}
