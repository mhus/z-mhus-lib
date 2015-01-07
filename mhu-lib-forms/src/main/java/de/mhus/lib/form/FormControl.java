package de.mhus.lib.form;

import de.mhus.lib.errors.MException;

public interface FormControl {

	void focused(LayoutElement element);
	
	void action(FormAction action);

	void wizzard(LayoutElement element);

	boolean validate(LayoutElement element, DataConnector dataConnector, Object value) throws MException;
	
}
