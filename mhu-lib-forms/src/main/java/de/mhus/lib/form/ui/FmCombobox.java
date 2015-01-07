package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.definition.FmElement;

public class FmCombobox extends FmElement {

	public FmCombobox(String name, IDefAttribute ... definitions) throws MException {
		super(name, definitions);
		setString("type", "combobox");
	}


}
