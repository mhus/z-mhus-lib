/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.logging.level;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.core.logging.MLogUtil;

public class ThreadMapperConfig implements LevelMapper {

	private LEVEL debug = LEVEL.DEBUG;
	private LEVEL error = LEVEL.ERROR;
	private LEVEL fatal = LEVEL.FATAL;
	private LEVEL info = LEVEL.INFO;
	private LEVEL trace = LEVEL.TRACE;
	private LEVEL warn = LEVEL.WARN;
	private boolean local = false;
	private long timeout = 0;
	private long timetout = 0;

	private String id = null;

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

	public void doConfigure(String source, String config) {
		if (config == null) return;
		config = config.trim().toUpperCase();
		
		if (config.equals(MLogUtil.MAP_LABEL)) {
			config = "";
		} else
		if (config.startsWith("MAP,")) // remove 'MAP,'
			config = config.substring(4);
		else
			config = "";
		
		// ident only
		if (config.length() > 0) {
			int pos = config.indexOf(',');
			if (pos < 0) {
				id = config;
				config = "";
			}
		}
		
		if (source == null) 
			source = MLogUtil.TRAIL_SOURCE_OTHER;
		source = source.toUpperCase();
		
        if (source.equals(MLogUtil.TRAIL_SOURCE_REST))
            config = MLogUtil.DEFAULT_TRAIL_REST.value();
        else
		if (config.length() == 0) {
			if (source.equals(MLogUtil.TRAIL_SOURCE_SHELL))
				config = MLogUtil.DEFAULT_TRAIL_SHELL.value();
			else
				config = MLogUtil.DEFAULT_TRAIL_CONFIG.value();
		}

        String[] parts = config.toUpperCase().split(",");

		if (parts.length > 0)
			trace = toLevel(parts[0]);
		if (parts.length > 1)
			debug = toLevel(parts[1]);
		if (parts.length > 2)
			info  = toLevel(parts[2]);
		if (parts.length > 3)
			warn  = toLevel(parts[3]);
		if (parts.length > 4)
			error = toLevel(parts[4]);
		if (parts.length > 5)
			fatal = toLevel(parts[5]);
		if (parts.length > 6)
			local = parts[6].equals("L");
		if (parts.length > 7)
			setTimeout( MCast.tolong(parts[7], 0));
		if (parts.length > 8)
			id = parts[8];

		if (id == null)
			id = source.substring(0,1) + MLogUtil.createTrailIdent();

		if (id.length() > 10) // max 10 characters
			id = id.substring(0,10);
		
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

	public String doSerialize() {
		return MLogUtil.MAP_LABEL + "," + 
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

	@Override
	public void prepareMessage(Log log, StringBuilder msg) {
		msg.append('{').append(id).append('}');
		msg.append('(').append(Thread.currentThread().getId()).append(')');
	}

	public String getTrailId() {
		return id;
	}

}
