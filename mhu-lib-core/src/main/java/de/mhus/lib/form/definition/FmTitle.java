package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.errors.MException;

public class FmTitle implements IDefAttribute {

	private String title;
	private String descritpion;

	public FmTitle(String title, String description) {
		this.title = title;
		this.descritpion = description;
	}
	
	@Override
	public void inject(DefComponent root) throws MException {
		if (title != null) root.setString("title", title);
		if (descritpion != null) root.setString("description", descritpion);
	}

}
