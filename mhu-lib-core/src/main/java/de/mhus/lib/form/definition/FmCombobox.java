package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.IDefAttribute;

public class FmCombobox extends FmElement {

	public static final String TYPE_COMBOBOX = "combobox";

	public FmCombobox(String name, String title, String description) {
		this(name, new FmNls(title, description));
	}

	public FmCombobox(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString(FmElement.TYPE, TYPE_COMBOBOX);
	}


}
