package de.mhus.lib.form;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;


/**
 * <p>FormAction class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FormAction {

	private LayoutActions parent;
	private ResourceNode config;
	private String title;
	private String description;
	private String nlsPrefix;
	private boolean enabled = true;
	private UiAction ui;

	/**
	 * <p>Constructor for FormAction.</p>
	 *
	 * @param parent a {@link de.mhus.lib.form.LayoutActions} object.
	 * @param action a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public FormAction(LayoutActions parent, ResourceNode action) throws MException {
		this.parent = parent;
		this.config = action;
		this.nlsPrefix = config.getExtracted("nls");
		title = parent.getNls().find(nlsPrefix + "_title");
		description = parent.getNls().find(nlsPrefix + "_description");
	}

	/**
	 * <p>Getter for the field <code>parent</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.LayoutComposite} object.
	 */
	public LayoutComposite getParent() {
		return parent;
	}
	
	/**
	 * <p>Getter for the field <code>config</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 */
	public ResourceNode getConfig() {
		return config;
	}
	
	/**
	 * <p>isPrimary.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isPrimary() {
		try {
			return config.getName().equals("primary");
		} catch (MException e) {
			throw new MRuntimeException(e);
		}
	}
	
	/**
	 * <p>isCancel.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isCancel() {
		try{
			return config.getName().equals("cancel");
		} catch (MException e) {
			throw new MRuntimeException(e);
		}
	}

	/**
	 * <p>isSecondary.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isSecondary() {
		return !isPrimary() && !isCancel();
	}
	
	/**
	 * <p>Getter for the field <code>title</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * <p>Getter for the field <code>description</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <p>doUpdate.</p>
	 *
	 * @param dc a {@link de.mhus.lib.form.DataConnector} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void doUpdate(DataConnector dc) throws MException {
		boolean x = enabled;
		enabled = dc.getBoolean(enabled);
		if (x != enabled && ui != null) ui.doUpdateEnabled();
	}

	/**
	 * <p>Getter for the field <code>ui</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.UiAction} object.
	 */
	public UiAction getUi() {
		return ui;
	}

	/**
	 * <p>Setter for the field <code>ui</code>.</p>
	 *
	 * @param ui a {@link de.mhus.lib.form.UiAction} object.
	 */
	public void setUi(UiAction ui) {
		if (this.ui != null) this.ui.doDisconnect();
		this.ui = ui;
		if (ui != null) ui.doConnect(this);
	}
	
	/**
	 * <p>isEnabled.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * <p>getOffset.</p>
	 *
	 * @return a int.
	 */
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
	
	/**
	 * <p>getColumns.</p>
	 *
	 * @return a int.
	 */
	public int getColumns() {
		if (isSecondary()) return 1;
		if (getParent().getColumns() > 6) return 2;
		return 1;
	}
	
	
}
