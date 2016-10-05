package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.IDefDefinition;

public class FmLayout100 extends FmElement {

	private static final long serialVersionUID = 1L;
	public FmLayout100(IDefDefinition ... definitions) {
		this("","","", definitions);
	}
	public FmLayout100(String name, String title, String description, IDefDefinition ... definitions) {
		super(name, new FmNls(title, description));
		addDefinition(new DefAttribute("layout", "100"));
		setString("type", "100");
		addDefinition(definitions);
	}

}
