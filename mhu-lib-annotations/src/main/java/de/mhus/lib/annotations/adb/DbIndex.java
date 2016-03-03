package de.mhus.lib.annotations.adb;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use this to add a list of indexes to a column. Use comma as separator. The simplest way
 * to name the indexes is by numbers. If the index starts with an 'u' the index will be defined
 * as an unique index. Do not name the index to long most database engines have restrictions
 * about the length of an index name.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DbIndex {

	/** Constant <code>UNIQUE="u"</code> */
	public static final String UNIQUE = "u";
	
	String[] value();

}
