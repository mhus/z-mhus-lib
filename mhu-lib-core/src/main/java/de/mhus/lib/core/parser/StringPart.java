package de.mhus.lib.core.parser;

import java.util.Map;

import de.mhus.lib.errors.MException;

/**
 * <p>StringPart interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface StringPart {

	/**
	 * <p>execute.</p>
	 *
	 * @param out a {@link java.lang.StringBuffer} object.
	 * @param attributes a {@link java.util.Map} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void execute(StringBuffer out, Map<String, Object> attributes) throws MException;

	/**
	 * <p>dump.</p>
	 *
	 * @param level a int.
	 * @param out a {@link java.lang.StringBuffer} object.
	 */
	public void dump(int level, StringBuffer out);
}
