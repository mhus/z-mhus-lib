package de.mhus.lib.logging.level;

import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.Log.LEVEL;

public class GeneralMapper implements LevelMapper {

	private ThreadMapperConfig config;
	
	@Override
	public LEVEL map(Log log, LEVEL level, Object... msg) {
		if (config == null) return level;
		return config.map(log, level, msg);
	}

	@Override
	public void prepareMessage(Log log, StringBuffer msg) {
		msg.append('(').append(Thread.currentThread().getId()).append(')');
	}

	public ThreadMapperConfig getConfig() {
		return config;
	}

	public void setConfig(ThreadMapperConfig config) {
		this.config = config;
	}

}
