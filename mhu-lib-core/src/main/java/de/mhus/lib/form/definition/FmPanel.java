package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.IDefDefinition;

public class FmPanel extends FmElement {

	private static final long serialVersionUID = 1L;

	public FmPanel(IDefDefinition ... definitions) {
		this("","","", definitions);
	}
	public FmPanel(String name, String title, String description, IDefDefinition ... definitions) {
		super(name, new FmNls(title, description));
		addDefinition(new DefAttribute("layout", "panel"));
		setString("type", "panel");
		addDefinition(definitions);
	}

}
