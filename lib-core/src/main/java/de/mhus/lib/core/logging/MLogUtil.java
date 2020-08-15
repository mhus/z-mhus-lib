/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.logging;

public class MLogUtil {

    //    public static CfgString DEFAULT_TRAIL_REST =
    //            new CfgString(Log.class, "restTrailConfig", "T,D,I,W,E,F,G,0");
    //    public static CfgString DEFAULT_TRAIL_SHELL =
    //            new CfgString(Log.class, "shellTrailConfig", "T,I,I,W,E,F,G,0");
    //    public static CfgString DEFAULT_TRAIL_CONFIG =
    //            new CfgString(Log.class, "defaultTrailConfig", "T,D,I,W,E,F,G,0");
    //
    //    public static final String TRAIL_SOURCE_SHELL = "S";
    //    public static final String TRAIL_SOURCE_REST = "R";
    //    public static final String TRAIL_SOURCE_JMS = "J";
    //    public static final String TRAIL_SOURCE_OTHER = "O";
    //    public static final String TRAIL_SOURCE_UI = "U";

    //    public static final String MAP_LABEL = "MAP";

    private static Log log = null;

    public static synchronized Log log() {
        if (log == null) {
            try {
                log = Log.getLog(MLogUtil.class);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return log;
    }

    //    public static void setTrailConfig() {
    //        setTrailConfig(null);
    //    }
    //
    //    public static void setTrailConfig(String parameters) {
    //        setTrailConfig(null, parameters);
    //    }

    //    public static void setTrailConfig(String source, String parameters) {
    //        IApi api = MApi.get();
    //        LevelMapper mapper = api.getLogFactory().getLevelMapper();
    //        if (mapper != null && mapper instanceof TrailLevelMapper) {
    //            TrailLevelMapper m = (TrailLevelMapper) mapper;
    //            //			ThreadMapperConfig config = new ThreadMapperConfig();
    //            //			if (parameters != null) {
    //            //				config.doConfigure(parameters);
    //            //			}
    //            m.doConfigureTrail(source, parameters);
    //        }
    //    }

    //    public static String getTrailConfig() {
    //        IApi api = MApi.get();
    //        LevelMapper mapper = api.getLogFactory().getLevelMapper();
    //        if (mapper != null && mapper instanceof TrailLevelMapper) {
    //            TrailLevelMapper m = (TrailLevelMapper) mapper;
    //            return m.doSerializeTrail();
    //        }
    //        return null;
    //    }
    //
    //    public static void releaseTrailConfig() {
    //        IApi api = MApi.get();
    //        LevelMapper mapper = api.getLogFactory().getLevelMapper();
    //        if (mapper != null && mapper instanceof TrailLevelMapper) {
    //            TrailLevelMapper m = (TrailLevelMapper) mapper;
    //            m.doResetTrail();
    //        }
    //    }
    //
    //    public static void resetAllTrailConfigs() {
    //        IApi api = MApi.get();
    //        LevelMapper mapper = api.getLogFactory().getLevelMapper();
    //        if (mapper != null && mapper instanceof TrailLevelMapper) {
    //            TrailLevelMapper m = (TrailLevelMapper) mapper;
    //            m.doResetAllTrails();
    //        }
    //    }
    //
    //    public static boolean isTrailLevelMapper() {
    //        IApi api = MApi.get();
    //        LevelMapper mapper = api.getLogFactory().getLevelMapper();
    //        return (mapper != null && mapper instanceof TrailLevelMapper);
    //    }

    public static void logStackTrace(Log log, String prefix, StackTraceElement[] stackTrace) {
        for (StackTraceElement element : stackTrace) {
            log.w(prefix, "  " + element);
        }
    }

    //    public static String createTrailIdent() {
    //        return MMath.toBasis36WithIdent((long) (Math.random() * 36 * 36 * 36 * 36), ++nextId,
    // 8);
    //    }
}
