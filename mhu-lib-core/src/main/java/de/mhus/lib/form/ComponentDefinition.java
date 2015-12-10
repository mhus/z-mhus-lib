package de.mhus.lib.form;

import de.mhus.lib.core.config.IConfig;

public class ComponentDefinition {

	private boolean fullSizeComponent;
	private String wizzard;
	private IConfig configurationForm;

	public boolean isFullSizeComonent() {
		return fullSizeComponent;
	}
	
	public String getWizzard() {
		return wizzard;
	}
	
	public IConfig getConfigurationForm() {
		return configurationForm;
	}
	
}
