package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefAttribute;

/**
 * <p>FmElement class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FmElement extends DefComponent {

	/** Constant <code>TITLE="title"</code> */
	public static final String TITLE = "title";
	/** Constant <code>DESCRIPTION="description"</code> */
	public static final String DESCRIPTION = "description";
	/** Constant <code>TYPE="type"</code> */
	public static final String TYPE = "type";
	/** Constant <code>NAME="name"</code> */
	public static final String NAME = "name";
	/** Constant <code>ENABLED="enabled"</code> */
	public static final String ENABLED = "enabled";
	/** Constant <code>SOURCES="sources"</code> */
	public static final String SOURCES = "sources";
	/** Constant <code>FULLWIDTH="fullwidth"</code> */
	public static final String FULLWIDTH = "fullwidth";
	/** Constant <code>TITLEINSIDE="titleinside"</code> */
	public static final String TITLEINSIDE = "titleinside";
	/** Constant <code>DEFAULT="default"</code> */
	public static final String DEFAULT = "default";
	/** Constant <code>TRUE="true"</code> */
	public static final String TRUE = "true";
	/** Constant <code>FALSE="false"</code> */
	public static final String FALSE = "false";
	/** Constant <code>ALLOW_NULL="allow_null"</code> */
	public static final String ALLOW_NULL = "allow_null";
	/** Constant <code>HEIGHT="height"</code> */
	public static final String HEIGHT = "height";
	/** Constant <code>WIZARD="wizard"</code> */
	public static final String WIZARD = "wizard";

	/**
	 * <p>Constructor for FmElement.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param definitions a {@link de.mhus.lib.core.definition.IDefAttribute} object.
	 */
	public FmElement(String name, IDefAttribute ... definitions) {
		super("element", definitions);
		setString("name", name);
	}


}
