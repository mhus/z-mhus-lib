package de.mhus.lib.core.strategy;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.config.IConfig;

public class DefaultTaskContext extends DefaultMonitor implements TaskContext {

	private IConfig config;
	private boolean test;
	private IProperties parameters;

	public void setTestOnly(boolean test) {
		this.test = test;
	}

	public void setConfig(IConfig config) {
		this.config = config;
	}

	public void setParameters(IProperties parameters) {
		this.parameters = parameters;
	}

	@Override
	public IConfig getConfig() {
		return config;
	}

	@Override
	public boolean isTestOnly() {
		return test;
	}

	@Override
	public IProperties getParameters() {
		return parameters;
	}

}