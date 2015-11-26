package de.mhus.lib.form;

public interface FormControl {

	void attachedForm(Form form);
	
	void focus(UiComponent component);
	
	boolean newValue(UiComponent component, Object newValue);

	void reverted(UiComponent component);
	
}
