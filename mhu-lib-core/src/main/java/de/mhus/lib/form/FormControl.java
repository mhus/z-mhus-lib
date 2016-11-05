package de.mhus.lib.form;

import de.mhus.lib.annotations.activator.DefaultImplementation;

@DefaultImplementation(FormControlAdapter.class)
public interface FormControl {

	void attachedForm(MForm form);
	
	void focus(UiComponent component);
	
	boolean newValue(UiComponent component, Object newValue);

	void reverted(UiComponent component);

	void newValueError(UiComponent component, Object newValue, Throwable t);

	void valueSet(UiComponent component);
	
}
