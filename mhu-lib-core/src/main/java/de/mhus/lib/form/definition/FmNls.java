package de.mhus.lib.form.definition;

import java.util.UUID;

import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.errors.MException;

public class FmNls extends DefAttribute {

	private String title;
	private String description;

	public FmNls(String value) {
		this(value,null,null);
	}
	
	public FmNls(String title, String description) {
		this(null,title,description);
	}
	
	public FmNls(String value, String title, String description) {
		super("nls", value == null ? UUID.randomUUID().toString() : value);
		this.title = title;
		this.description = description;

	}

	@Override
	public void inject(DefComponent root) throws MException {
		super.inject(root);
		if (title != null) root.setString("caption", title);
		if (description != null) root.setString("description", description);
	}

}
