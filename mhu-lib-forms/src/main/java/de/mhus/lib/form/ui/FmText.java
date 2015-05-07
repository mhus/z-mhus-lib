package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.definition.FmElement;

public class FmText extends FmElement {

	public FmText(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type", "text");
	}


}
