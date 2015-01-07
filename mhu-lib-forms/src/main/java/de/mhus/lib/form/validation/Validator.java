package de.mhus.lib.form.validation;

import de.mhus.lib.form.FormControl;
import de.mhus.lib.form.LayoutElement;

public interface Validator {

	public boolean validate(FormControl control, LayoutElement element, Object value);

}
