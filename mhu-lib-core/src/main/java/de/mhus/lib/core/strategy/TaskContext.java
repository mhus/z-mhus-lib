package de.mhus.lib.core.strategy;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.config.IConfig;

public interface TaskContext extends Monitor {

	IConfig getConfig();
	boolean isTestOnly();
	
	MProperties getAttributes();
	
}
