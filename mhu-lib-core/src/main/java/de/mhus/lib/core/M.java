package de.mhus.lib.core;

import java.util.function.Function;

import de.mhus.lib.core.pojo.MPojo;

/**
 * This is a shortcut class to call methods without
 * obfuscating the source code. For some reasons this 
 * makes sense.
 * 
 * @author mikehummel
 *
 */
public class M {

	/**
	 * Return a string cascading the names of the getters (without 'get' prefix).
	 * and joined with underscore.
	 * 
	 * This is used to create identifiers for MForm or Adb.
	 * 
	 * @param getters
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> String n(Function<T,?> ... getters ) {
		StringBuilder out = new StringBuilder();
		for (Function<T, ?> getter : getters) {
			if (out.length() > 0) out.append('_');
			out.append(MPojo.toAttributeName(getter));
		}
		return out.toString();
	}
}
