package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.IDefAttribute;

public class FmText extends FmElement {

	public FmText(String name, String title, String description, IDefAttribute ... definitions) {
		this(name, new FmNls(title, description));
		addDefinition(definitions);
	}
	
	public FmText(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type", "text");
	}


}
