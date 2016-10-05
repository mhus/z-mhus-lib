package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.IDefAttribute;

public class FmOptions extends FmElement {

	public FmOptions(String name, String title, String description, IDefAttribute ... definitions) {
		this(name, new FmNls(title, description));
		addDefinition(definitions);
	}
	
	public FmOptions(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type", "options");
	}


}
