package de.mhus.lib.logging.mapper;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.Log.LEVEL;

public class ThreadMapperConfig implements LevelMapper {

	public static final String MAP_LABEL = "MAP";

	private LEVEL debug = LEVEL.INFO;
	private LEVEL error = LEVEL.ERROR;
	private LEVEL fatal = LEVEL.FATAL;
	private LEVEL info = LEVEL.INFO;
	private LEVEL trace = LEVEL.INFO;
	private LEVEL warn = LEVEL.WARN;
	private boolean local = false;
	private long timeout = 0;
	private long timetout = 0;

	public boolean isTimedOut() {
		if (timetout <= 0)
			return false;
		return System.currentTimeMillis() >= timetout;
	}
	
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

	public void doConfigure(String config) {
		if (config == null) return;
		String[] parts = config.toUpperCase().split(",");
		if (parts.length <= 8) return;
		trace = LEVEL.valueOf(parts[1]);
		debug = LEVEL.valueOf(parts[2]);
		info  = LEVEL.valueOf(parts[3]);
		warn  = LEVEL.valueOf(parts[4]);
		error = LEVEL.valueOf(parts[5]);
		fatal = LEVEL.valueOf(parts[6]);
		local = parts[7].equals("LOCAL");
		setTimeout( MCast.tolong(parts[8], 0));
	}

	public String doSerialize() {
		return MAP_LABEL + "," + 
				trace.name() + "," + 
				debug.name() + "," + 
				info.name() + "," + 
				warn.name() + "," + 
				error.name() + "," + 
				fatal.name() + "," +
				(local ? "LOCAL" : "GLOBAL") + "," +
				timeout;
	}
	
	@Override
	public String toString() {
		return doSerialize();
	}

	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		if (timeout <= 0) {
			timetout = 0;
		} else {
			timetout = System.currentTimeMillis() + timeout;
		}
		this.timeout = timeout;
	}

}
