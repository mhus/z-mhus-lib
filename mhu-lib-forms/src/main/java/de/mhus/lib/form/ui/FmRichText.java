package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.form.definition.FmElement;
import de.mhus.lib.form.definition.FmNls;

public class FmRichText extends FmElement {

	public FmRichText(String name, String title, String description) {
		this(name, new FmNls(title, description));
	}
	
	public FmRichText(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type", "richtext");
	}


}
