package de.mhus.lib.core.strategy;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.config.IConfig;

public interface TaskContext extends Monitor {

	IConfig getConfig();
	boolean isTestOnly();
	
	AbstractProperties getParameters();
	
	void addErrorMessage(String msg);
	String getErrorMessage();
	
}
