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
package de.mhus.lib.core.logging;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MMath;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.core.system.IApi;
import de.mhus.lib.logging.level.ThreadMapperConfig;

public class MLogUtil {

    public static CfgString DEFAULT_TRAIL_REST = new CfgString(Log.class, "restTrailConfig", "T,I,I,W,E,F,G,0");
    public static CfgString DEFAULT_TRAIL_CONFIG = new CfgString(Log.class, "defaultTrailConfig", "T,I,I,W,E,F,G,0");

	public static final String TRAIL_SOURCE_SHELL = "s";
    public static final String TRAIL_SOURCE_REST = "r";
    public static final String TRAIL_SOURCE_JMS = "j";
    public static final String TRAIL_SOURCE_OTHER = "o";
    public static final String TRAIL_SOURCE_UI = "u";
    
    private static long nextId = 0;

    private static Log log = null;
	
	public synchronized static Log log() {
		if (log == null) {
			try {
				log = Log.getLog(MLogUtil.class);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		return log;
	}
	
	public static void setTrailConfig() {
		setTrailConfig(null);
	}
	
    public static void setTrailConfig(String source, String parameters) {
        setTrailConfig(createTrailConfig(source, parameters));
    }
    
	public static void setTrailConfig(String parameters) {
		IApi api = MApi.get();
		LevelMapper mapper = api.getLogFactory().getLevelMapper();
		if (mapper != null && mapper instanceof TrailLevelMapper) {
			TrailLevelMapper m = (TrailLevelMapper)mapper;
//			ThreadMapperConfig config = new ThreadMapperConfig();
//			if (parameters != null) {
//				config.doConfigure(parameters);
//			}
			m.doConfigureTrail(parameters);
		}
	}

	public static String getTrailConfig() {
		IApi api = MApi.get();
		LevelMapper mapper = api.getLogFactory().getLevelMapper();
		if (mapper != null && mapper instanceof TrailLevelMapper) {
			TrailLevelMapper m = (TrailLevelMapper)mapper;
			return m.doSerializeTrail();
		}
		return null;
	}

	public static void releaseTrailConfig() {
		IApi api = MApi.get();
		LevelMapper mapper = api.getLogFactory().getLevelMapper();
		if (mapper != null && mapper instanceof TrailLevelMapper) {
			TrailLevelMapper m = (TrailLevelMapper)mapper;
			m.doResetTrail();
		}
	}

	public static void resetAllTrailConfigs() {
		IApi api = MApi.get();
		LevelMapper mapper = api.getLogFactory().getLevelMapper();
		if (mapper != null && mapper instanceof TrailLevelMapper) {
			TrailLevelMapper m = (TrailLevelMapper)mapper;
			m.doResetAllTrails();
		}
	}
	
	public static boolean isTrailLevelMapper() {
		IApi api = MApi.get();
		LevelMapper mapper = api.getLogFactory().getLevelMapper();
		return (mapper != null && mapper instanceof TrailLevelMapper);
	}
	
	public static void logStackTrace(Log log, String prefix, StackTraceElement[] stackTrace) {
		for (StackTraceElement element : stackTrace) {
			log.w(prefix,"  " + element);
		}
		
	}

    public static String createTrailConfig(String source, String parameters) {
        if (parameters == null || parameters.length() == 0)
            return null;
        if (source == null || source.length() == 0) source = TRAIL_SOURCE_OTHER;
        String[] parts = parameters.split(",");
        if (parts.length < 2) return null;
        if (parts[1].length() == 0 || parts[1].equals("?"))
            parts[1] = source.substring(0,1) + createTrailIdent();
        else if (!source.equals(TRAIL_SOURCE_REST))
            return parameters; // use the same
        String config = null;
        if (source.equals(TRAIL_SOURCE_REST))
            config = DEFAULT_TRAIL_REST.value();
        else
        if (parts.length > 2)
            config = MString.join(parts,2,parts.length,",");
        else
            config = DEFAULT_TRAIL_CONFIG.value();

        return ThreadMapperConfig.MAP_LABEL + "," + parts[1] + config;
    }

    public static String createTrailIdent() {
        return MMath.toBasis36WithIdent( (long) (Math.random() * 36 * 36 * 36 * 36), ++nextId, 8 );
    }

}
