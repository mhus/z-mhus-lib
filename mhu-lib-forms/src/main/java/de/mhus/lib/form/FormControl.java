package de.mhus.lib.form;

import de.mhus.lib.errors.MException;

/**
 * <p>FormControl interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface FormControl {

	/**
	 * <p>focused.</p>
	 *
	 * @param element a {@link de.mhus.lib.form.LayoutElement} object.
	 */
	void focused(LayoutElement element);
	
	/**
	 * <p>action.</p>
	 *
	 * @param action a {@link de.mhus.lib.form.FormAction} object.
	 */
	void action(FormAction action);

	/**
	 * <p>wizard.</p>
	 *
	 * @param element a {@link de.mhus.lib.form.LayoutElement} object.
	 * @since 3.2.9
	 */
	void wizard(LayoutElement element);

	/**
	 * <p>validate.</p>
	 *
	 * @param element a {@link de.mhus.lib.form.LayoutElement} object.
	 * @param dataConnector a {@link de.mhus.lib.form.DataConnector} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a boolean.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	boolean validate(LayoutElement element, DataConnector dataConnector, Object value) throws MException;
	
}
