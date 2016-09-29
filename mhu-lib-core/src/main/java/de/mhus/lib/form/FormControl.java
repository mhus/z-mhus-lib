package de.mhus.lib.form;

import de.mhus.lib.annotations.activator.DefaultImplementation;

/**
 * <p>FormControl interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
@DefaultImplementation(FormControlAdapter.class)
public interface FormControl {

	/**
	 * <p>attachedForm.</p>
	 *
	 * @param form a {@link de.mhus.lib.form.Form} object.
	 */
	void attachedForm(Form form);
	
	/**
	 * <p>focus.</p>
	 *
	 * @param component a {@link de.mhus.lib.form.UiComponent} object.
	 */
	void focus(UiComponent component);
	
	/**
	 * <p>newValue.</p>
	 *
	 * @param component a {@link de.mhus.lib.form.UiComponent} object.
	 * @param newValue a {@link java.lang.Object} object.
	 * @return a boolean.
	 */
	boolean newValue(UiComponent component, Object newValue);

	/**
	 * <p>reverted.</p>
	 *
	 * @param component a {@link de.mhus.lib.form.UiComponent} object.
	 */
	void reverted(UiComponent component);

	/**
	 * <p>newValueError.</p>
	 *
	 * @param component a {@link de.mhus.lib.form.UiComponent} object.
	 * @param newValue a {@link java.lang.Object} object.
	 * @param t a {@link java.lang.Throwable} object.
	 */
	void newValueError(UiComponent component, Object newValue, Throwable t);

	/**
	 * <p>valueSet.</p>
	 *
	 * @param component a {@link de.mhus.lib.form.UiComponent} object.
	 */
	void valueSet(UiComponent component);
	
}
