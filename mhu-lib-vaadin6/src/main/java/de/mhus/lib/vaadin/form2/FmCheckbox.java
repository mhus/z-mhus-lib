package de.mhus.lib.vaadin.form2;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.definition.FmElement;

public class FmCheckbox extends FmElement {

	public FmCheckbox(String name, IDefAttribute ... definitions) throws MException {
		super(name, definitions);
		setString("type","checkbox");
	}
	
	public FmCheckbox defaultValue(boolean in) throws MException {
		setBoolean("default", in);
		return this;
	}
	
}
