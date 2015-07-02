package de.mhus.lib.core.strategy;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.config.IConfig;

public interface TaskContext extends Monitor {

	IConfig getConfig();
	boolean isTestOnly();
	
	IProperties getParameters();
	
	void addErrorMessage(String msg);
	String getErrorMessage();
	
}
