package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.IDefAttribute;

public class FmLabel extends FmElement {

	public FmLabel(String name, String title, String description, IDefAttribute ... definitions) {
		this(name, new FmNls(title, description));
		addDefinition(definitions);
	}
	
	public FmLabel(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type", "label");
	}


}
