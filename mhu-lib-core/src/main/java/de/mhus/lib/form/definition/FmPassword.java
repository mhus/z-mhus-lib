package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.IDefAttribute;

public class FmPassword extends FmElement {

	public FmPassword(String name, String title, String description) {
		this(name, new FmNls(title, description));
	}

	public FmPassword(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type", "password");
	}


}
