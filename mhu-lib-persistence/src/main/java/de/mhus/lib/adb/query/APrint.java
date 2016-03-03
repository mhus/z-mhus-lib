package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

/**
 * <p>Abstract APrint class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class APrint {

	/**
	 * <p>print.</p>
	 *
	 * @param query a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @param buffer a {@link java.lang.StringBuffer} object.
	 */
	public abstract void print(AQuery<?> query, StringBuffer buffer);
	/**
	 * <p>getAttributes.</p>
	 *
	 * @param map a {@link de.mhus.lib.core.parser.AttributeMap} object.
	 */
	public abstract void getAttributes(AttributeMap map);

}
