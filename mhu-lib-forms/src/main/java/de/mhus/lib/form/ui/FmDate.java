package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.form.definition.FmDefaultSources;
import de.mhus.lib.form.definition.FmElement;
import de.mhus.lib.form.definition.FmNls;

public class FmDate extends FmElement {

	public FmDate(String name, String title, String description) {
		this(name, new FmNls(title, description), new FmDefaultSources());
	}

	public FmDate(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type", "date");
	}


}
