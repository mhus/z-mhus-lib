package de.mhus.lib.form;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;

public abstract class UiComponent {

	private Form form;
	private IConfig config;

	public UiComponent(Form form, IConfig config) {
		this.form = form;
		this.config = config;
	}

	public Form getForm() {
		return form;
	}

	public IConfig getConfig() {
		return config;
	}

	public abstract void doUpdate() throws MException;

	public abstract void setVisible(boolean visible) throws MException;
	
	public abstract boolean isVisible() throws MException;
	
	public abstract void setEnabled(boolean enabled) throws MException;
	
	public abstract boolean isEnabled() throws MException;
	
}
