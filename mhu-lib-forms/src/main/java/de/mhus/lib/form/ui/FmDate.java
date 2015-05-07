package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.definition.FmElement;

public class FmDate extends FmElement {

	public FmDate(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type", "date");
	}


}
