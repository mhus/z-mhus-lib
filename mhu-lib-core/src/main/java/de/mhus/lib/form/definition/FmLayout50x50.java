package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.IDefDefinition;

public class FmLayout50x50 extends FmElement {

	private static final long serialVersionUID = 1L;

	public FmLayout50x50(IDefDefinition ... definitions) {
		this("","","", definitions);
	}
	
	public FmLayout50x50(String name, String title, String description, IDefDefinition ... definitions) {
		super(name, new FmNls(title, description));
		addDefinition(new DefAttribute("layout", "50x50"));
		setString("type", "50x50");
		addDefinition(definitions);
	}

}
