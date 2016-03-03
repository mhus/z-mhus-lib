package de.mhus.lib.form.validation;

import de.mhus.lib.form.FormControl;
import de.mhus.lib.form.LayoutElement;

/**
 * <p>Validator interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface Validator {

	/**
	 * <p>validate.</p>
	 *
	 * @param control a {@link de.mhus.lib.form.FormControl} object.
	 * @param element a {@link de.mhus.lib.form.LayoutElement} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a boolean.
	 */
	public boolean validate(FormControl control, LayoutElement element, Object value);

}
