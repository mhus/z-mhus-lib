package de.mhus.lib.form;

import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.errors.MException;

/**
 * <p>Abstract UiElement class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class UiElement extends MObject {

	private LayoutElement element;

	/**
	 * <p>Getter for the field <code>element</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.LayoutElement} object.
	 */
	public LayoutElement getElement() {
		return element;
	}

	/**
	 * <p>Setter for the field <code>element</code>.</p>
	 *
	 * @param element a {@link de.mhus.lib.form.LayoutElement} object.
	 */
	public void setElement(LayoutElement element) {
		this.element = element;
		if (element == null) 
			doDisconnect();
		else
			doConnect();
	}

	/**
	 * <p>doConnect.</p>
	 */
	protected abstract void doConnect();

	/**
	 * <p>doDisconnect.</p>
	 */
	protected abstract void doDisconnect();

	/**
	 * <p>doUpdate.</p>
	 *
	 * @param data a {@link de.mhus.lib.form.DataConnector} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public abstract void doUpdate(DataConnector data) throws MException;
	
	/**
	 * <p>setErrorMessage.</p>
	 *
	 * @param msg a {@link java.lang.String} object.
	 */
	public abstract void setErrorMessage(String msg);
	
	/**
	 * <p>equals.</p>
	 *
	 * @param arg1 a {@link java.lang.Object} object.
	 * @param arg2 a {@link java.lang.Object} object.
	 * @return a boolean.
	 */
	public boolean equals(Object arg1, Object arg2) {
		if (arg1 == null && arg2 == null) return true;
		if (arg1 == null) return false;
		return arg1.equals(arg2);
	}

}
