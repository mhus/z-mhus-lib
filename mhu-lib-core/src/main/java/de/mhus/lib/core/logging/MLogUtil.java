package de.mhus.lib.core.logging;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.system.ISingleton;

public class MLogUtil {

	public static void setTrailConfig(String parameters) {
		ISingleton singleton = MSingleton.get();
		LevelMapper mapper = singleton.getLogFactory().getLevelMapper();
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
		ISingleton singleton = MSingleton.get();
		LevelMapper mapper = singleton.getLogFactory().getLevelMapper();
		if (mapper != null && mapper instanceof TrailLevelMapper) {
			TrailLevelMapper m = (TrailLevelMapper)mapper;
			return m.doSerializeTrail();
		}
		return null;
	}

	public static void releaseTrailConfig() {
		ISingleton singleton = MSingleton.get();
		LevelMapper mapper = singleton.getLogFactory().getLevelMapper();
		if (mapper != null && mapper instanceof TrailLevelMapper) {
			TrailLevelMapper m = (TrailLevelMapper)mapper;
			m.doResetTrail();
		}
	}

	public static boolean isTrailLevelMapper() {
		ISingleton singleton = MSingleton.get();
		LevelMapper mapper = singleton.getLogFactory().getLevelMapper();
		return (mapper != null && mapper instanceof TrailLevelMapper);
	}
	
}
