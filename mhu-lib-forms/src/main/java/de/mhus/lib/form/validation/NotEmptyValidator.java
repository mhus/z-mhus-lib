package de.mhus.lib.form.validation;

import de.mhus.lib.core.MString;
import de.mhus.lib.form.FormControl;
import de.mhus.lib.form.LayoutElement;

/**
 * <p>NotEmptyValidator class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class NotEmptyValidator implements Validator {

	/** {@inheritDoc} */
	@Override
	public boolean validate(FormControl control, LayoutElement element, Object value) {
		return (value != null && !MString.isEmpty(value.toString()));
	}

}
