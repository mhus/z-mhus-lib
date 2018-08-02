package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.IDefAttribute;

public class FmAction extends IFmElement {

	private static final long serialVersionUID = 1L;

	public FmAction(String name, String action, String title, String description, IDefAttribute ... definitions) {
		this(name, new FaNls(name + ".title=" + title, name + ".description=" + description));
		setString("action", action);
		addDefinition(definitions);
	}
	
	public FmAction(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type", "action");
	}

}
