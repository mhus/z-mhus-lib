package de.mhus.lib.core.logging;

import de.mhus.lib.core.logging.Log.LEVEL;

/**
 * <p>LevelMapper interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public interface LevelMapper {

	/**
	 * <p>map.</p>
	 *
	 * @param log a {@link de.mhus.lib.core.logging.Log} object.
	 * @param level a {@link de.mhus.lib.core.logging.Log.LEVEL} object.
	 * @param msg a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.core.logging.Log.LEVEL} object.
	 */
	LEVEL map(Log log, LEVEL level, Object ... msg);
	
	/**
	 * <p>prepareMessage.</p>
	 *
	 * @param log a {@link de.mhus.lib.core.logging.Log} object.
	 * @param msg a {@link java.lang.StringBuffer} object.
	 */
	void prepareMessage(Log log, StringBuffer msg);
	
}
