package de.mhus.lib.form;

import de.mhus.lib.core.config.IConfig;

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

	public abstract void doUpdate();

	public abstract void setVisible(boolean visible);
	
	public abstract boolean isVisible();
	
	public abstract void setEnabled(boolean enabled);
	
	public abstract boolean isEnabled();
	
}
