package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.IDefDefinition;

public class FmLayoutTabs extends FmElement {

	private static final long serialVersionUID = 1L;

	public FmLayoutTabs(IDefDefinition ... definitions) {
		this("","","", definitions);
	}
	public FmLayoutTabs(String name, String title, String description, IDefDefinition ... definitions) {
		super(name, new FmNls(title, description));
		addDefinition(new DefAttribute("layout", "tabs"));
		setString("type", "tabs");
		addDefinition(definitions);
	}

}
