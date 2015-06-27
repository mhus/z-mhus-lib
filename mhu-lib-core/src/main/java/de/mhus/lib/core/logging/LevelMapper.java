package de.mhus.lib.core.logging;

import de.mhus.lib.core.logging.Log.LEVEL;

public interface LevelMapper {

	LEVEL map(Log log, LEVEL level, Object ... msg);

	
}
