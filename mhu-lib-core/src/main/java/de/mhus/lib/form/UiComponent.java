package de.mhus.lib.form;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.errors.MException;

/**
 * <p>Abstract UiComponent class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public abstract class UiComponent {

	/** Constant <code>FULL_SIZE="fullSize"</code> */
	public static final String FULL_SIZE = "fullSize";
	/** Constant <code>FULL_SIZE_DEFAULT="fullSizeDefault"</code> */
	public static final String FULL_SIZE_DEFAULT = "fullSizeDefault";
	private static final String WIZARD = null;
	
	private Form form;
	private IConfig config;

	/**
	 * <p>doInit.</p>
	 *
	 * @param form a {@link de.mhus.lib.form.Form} object.
	 * @param config a {@link de.mhus.lib.core.config.IConfig} object.
	 */
	public void doInit(Form form, IConfig config) {
		this.form = form;
		this.config = config;
	}

	/**
	 * <p>Getter for the field <code>form</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.Form} object.
	 */
	public Form getForm() {
		return form;
	}

	/**
	 * <p>Getter for the field <code>config</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.config.IConfig} object.
	 */
	public IConfig getConfig() {
		return config;
	}

	/**
	 * <p>doRevert.</p>
	 *
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public abstract void doRevert() throws MException;

	/**
	 * <p>doUpdateValue.</p>
	 *
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public abstract void doUpdateValue() throws MException;
	
	/**
	 * <p>doUpdateMetadata.</p>
	 *
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public abstract void doUpdateMetadata() throws MException;
	
	/**
	 * <p>setVisible.</p>
	 *
	 * @param visible a boolean.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public abstract void setVisible(boolean visible) throws MException;
	
	/**
	 * <p>isVisible.</p>
	 *
	 * @return a boolean.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public abstract boolean isVisible() throws MException;
	
	/**
	 * <p>setEnabled.</p>
	 *
	 * @param enabled a boolean.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public abstract void setEnabled(boolean enabled) throws MException;
	
	/**
	 * <p>setEditable.</p>
	 *
	 * @param editable a boolean.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public abstract void setEditable(boolean editable) throws MException;
	
	/**
	 * <p>isEnabled.</p>
	 *
	 * @return a boolean.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public abstract boolean isEnabled() throws MException;

	/**
	 * <p>isFullSize.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isFullSize() {
		return config.getBoolean(FULL_SIZE, config.getBoolean(FULL_SIZE_DEFAULT, false));
	}

	/**
	 * <p>getWizard.</p>
	 *
	 * @return a {@link de.mhus.lib.form.UiWizard} object.
	 */
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

	/**
	 * <p>getName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return config.getString("name", "");
	}
	
	/**
	 * <p>setError.</p>
	 *
	 * @param error a {@link java.lang.String} object.
	 */
	public abstract void setError(String error);
	
	/**
	 * <p>clearError.</p>
	 */
	public abstract void clearError();


}
