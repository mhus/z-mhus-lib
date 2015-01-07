package de.mhus.lib.form.validation;

import de.mhus.lib.core.MString;
import de.mhus.lib.form.FormControl;
import de.mhus.lib.form.LayoutElement;

public class NotEmptyValidator implements Validator {

	@Override
	public boolean validate(FormControl control, LayoutElement element, Object value) {
		return (value != null && !MString.isEmpty(value.toString()));
	}

}
