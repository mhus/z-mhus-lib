package de.mhus.lib.form;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.errors.MException;

public abstract class UiComponent {

	public static final String FULL_SIZE = "fullSize";
	public static final String FULL_SIZE_DEFAULT = "fullSizeDefault";
	private static final String WIZARD = null;
	
	private Form form;
	private IConfig config;

	public void doInit(Form form, IConfig config) {
		this.form = form;
		this.config = config;
	}

	public Form getForm() {
		return form;
	}

	public IConfig getConfig() {
		return config;
	}

	public abstract void doRevert() throws MException;

	public abstract void setVisible(boolean visible) throws MException;
	
	public abstract boolean isVisible() throws MException;
	
	public abstract void setEnabled(boolean enabled) throws MException;
	
	public abstract boolean isEnabled() throws MException;

	public boolean isFullSize() {
		return config.getBoolean(FULL_SIZE, config.getBoolean(FULL_SIZE_DEFAULT, false));
	}

	public UiWizard getWizard() {
		Object obj = config.getProperty(WIZARD);
		if (obj == null) return null;
		if (obj instanceof UiWizard) return (UiWizard)obj;
		try {
			if (obj instanceof String) return getForm().getAdapterProvider().createWizard((String)obj);
		} catch (Exception e) {
			MLogUtil.log().d(e);
		}
		return null; // TODO
	}

	public String getName() {
		return config.getString("name", "");
	}
	
}
