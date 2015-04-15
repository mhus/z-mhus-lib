package de.mhus.lib.vaadin.form2;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.definition.FmElement;

public class FmText extends FmElement {

	public FmText(String name, IDefAttribute ... definitions) throws MException {
		super(name, definitions);
		setString("type", "text");
	}


}
