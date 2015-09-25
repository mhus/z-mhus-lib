package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefAttribute;

public class FmElement extends DefComponent {

	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String TYPE = "type";
	public static final String NAME = "name";
	public static final String ENABLED = "enabled";
	public static final String SOURCES = "sources";
	public static final String FULLWIDTH = "fullwidth";
	public static final String TITLEINSIDE = "titleinside";
	public static final String DEFAULT = "default";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String ALLOW_NULL = "allow_null";
	public static final String HEIGHT = "height";
	public static final String WIZARD = "wizard";

	public FmElement(String name, IDefAttribute ... definitions) {
		super("element", definitions);
		setString("name", name);
	}


}
