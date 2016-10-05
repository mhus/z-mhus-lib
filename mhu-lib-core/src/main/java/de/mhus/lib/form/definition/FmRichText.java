package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.IDefAttribute;

public class FmRichText extends FmElement {

	public FmRichText(String name, String title, String description, IDefAttribute ... definitions) {
		this(name, new FmNls(title, description));
		addDefinition(definitions);
	}
	
	public FmRichText(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type", "richtext");
	}


}
