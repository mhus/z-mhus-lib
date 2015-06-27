package de.mhus.lib.core.logging;

import de.mhus.lib.core.logging.Log.LEVEL;

public interface LevelMapper {

	LEVEL map(LEVEL level, Object ... msg);

}
