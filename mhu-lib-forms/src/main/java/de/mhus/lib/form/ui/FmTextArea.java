package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.form.definition.FmElement;
import de.mhus.lib.form.definition.FmNls;

public class FmTextArea extends FmElement {

	public FmTextArea(String name, String title, String description) {
		this(name, new FmNls(title, description));
	}
	
	public FmTextArea(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type", "textarea");
	}


}
