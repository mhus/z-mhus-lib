package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.IDefAttribute;

public class FmTextArea extends FmElement {

	public FmTextArea(String name, String title, String description) {
		this(name, new FmNls(title, description));
	}
	
	public FmTextArea(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type", "textarea");
	}


}
