package de.mhus.lib.form;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;

public abstract class UiComponent {

	public static final String FULL_SIZE = "fullSize";
	public static final String FULL_SIZE_DEFAULT = "fullSizeDefault";
	
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

	public boolean isFullSize() {
		return config.getBoolean(FULL_SIZE, config.getBoolean(FULL_SIZE_DEFAULT, false));
	}

	public UiWizard getWizard() {
		return null; // TODO
	}
	
}
