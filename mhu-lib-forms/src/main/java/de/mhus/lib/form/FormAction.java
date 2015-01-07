package de.mhus.lib.form;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;


public class FormAction {

	private LayoutActions parent;
	private ResourceNode config;
	private String title;
	private String description;
	private String nlsPrefix;
	private boolean enabled = true;
	private UiAction ui;

	public FormAction(LayoutActions parent, ResourceNode action) throws MException {
		this.parent = parent;
		this.config = action;
		this.nlsPrefix = config.getExtracted("nls");
		title = parent.getNls().find(nlsPrefix + "_title");
		description = parent.getNls().find(nlsPrefix + "_description");
	}

	public LayoutComposite getParent() {
		return parent;
	}
	
	public ResourceNode getConfig() {
		return config;
	}
	
	public boolean isPrimary() {
		try {
			return config.getName().equals("primary");
		} catch (MException e) {
			throw new MRuntimeException(e);
		}
	}
	
	public boolean isCancel() {
		try{
			return config.getName().equals("cancel");
		} catch (MException e) {
			throw new MRuntimeException(e);
		}
	}

	public boolean isSecondary() {
		return !isPrimary() && !isCancel();
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}

	public void doUpdate(DataConnector dc) throws MException {
		boolean x = enabled;
		enabled = dc.getBoolean(enabled);
		if (x != enabled && ui != null) ui.doUpdateEnabled();
	}

	public UiAction getUi() {
		return ui;
	}

	public void setUi(UiAction ui) {
		if (this.ui != null) this.ui.doDisconnect();
		this.ui = ui;
		if (ui != null) ui.doConnect(this);
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public int getOffset() {
		if (isPrimary()) {
			return getParent().getLabelColums();
		} else
		if (isCancel()) {
			return getParent().getColumns()-getColumns();
		} else {
			return 0;
		}
	}
	
	public int getColumns() {
		if (isSecondary()) return 1;
		if (getParent().getColumns() > 6) return 2;
		return 1;
	}
	
	
}
