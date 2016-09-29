package de.mhus.lib.core.logging;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.system.ISingleton;

/**
 * <p>MLogUtil class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MLogUtil {

	private static Log log = Log.getLog(MLogUtil.class);
	
	/**
	 * <p>log.</p>
	 *
	 * @return a {@link de.mhus.lib.core.logging.Log} object.
	 */
	public static Log log() {
		return log;
	}
	
	/**
	 * <p>setTrailConfig.</p>
	 */
	public static void setTrailConfig() {
		setTrailConfig(null);
	}
	
	/**
	 * <p>setTrailConfig.</p>
	 *
	 * @param parameters a {@link java.lang.String} object.
	 */
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

	/**
	 * <p>getTrailConfig.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public static String getTrailConfig() {
		ISingleton singleton = MSingleton.get();
		LevelMapper mapper = singleton.getLogFactory().getLevelMapper();
		if (mapper != null && mapper instanceof TrailLevelMapper) {
			TrailLevelMapper m = (TrailLevelMapper)mapper;
			return m.doSerializeTrail();
		}
		return null;
	}

	/**
	 * <p>releaseTrailConfig.</p>
	 */
	public static void releaseTrailConfig() {
		ISingleton singleton = MSingleton.get();
		LevelMapper mapper = singleton.getLogFactory().getLevelMapper();
		if (mapper != null && mapper instanceof TrailLevelMapper) {
			TrailLevelMapper m = (TrailLevelMapper)mapper;
			m.doResetTrail();
		}
	}

	/**
	 * <p>isTrailLevelMapper.</p>
	 *
	 * @return a boolean.
	 */
	public static boolean isTrailLevelMapper() {
		ISingleton singleton = MSingleton.get();
		LevelMapper mapper = singleton.getLogFactory().getLevelMapper();
		return (mapper != null && mapper instanceof TrailLevelMapper);
	}
	
	/**
	 * <p>logStackTrace.</p>
	 *
	 * @param log a {@link de.mhus.lib.core.logging.Log} object.
	 * @param prefix a {@link java.lang.String} object.
	 * @param stackTrace an array of {@link java.lang.StackTraceElement} objects.
	 */
	public static void logStackTrace(Log log, String prefix, StackTraceElement[] stackTrace) {
		for (StackTraceElement element : stackTrace) {
			log.w(prefix,"  " + element);
		}
		
	}

}
