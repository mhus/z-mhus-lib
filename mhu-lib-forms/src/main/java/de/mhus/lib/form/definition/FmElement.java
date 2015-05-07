package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.errors.MException;

public class FmElement extends DefComponent {

	public FmElement(String name, IDefAttribute ... definitions) {
		super("element", definitions);
		setString("name", name);
	}


}
