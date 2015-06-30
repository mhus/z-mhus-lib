package de.mhus.lib.core.logging;

public interface ParameterEntryMapper {

	/**
	 * Return a new object if you are able to map this object. If not return null.
	 *
	 * @param in
	 * @return
	 */
	Object map(Object in);
	
}
