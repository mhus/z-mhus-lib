package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.IDefAttribute;

public class FmLink extends FmElement {

	public FmLink(String name, String label, String title, String description, IDefAttribute ... definitions) {
		this(name, new FmNls(title, description));
		setString("label", label);
		addDefinition(definitions);
	}
	
	public FmLink(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type", "link");
	}


}
