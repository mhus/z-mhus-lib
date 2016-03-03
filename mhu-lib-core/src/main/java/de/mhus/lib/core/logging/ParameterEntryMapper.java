package de.mhus.lib.core.logging;

/**
 * <p>ParameterEntryMapper interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public interface ParameterEntryMapper {

	/**
	 * Return a new object if you are able to map this object. If not return null.
	 *
	 * @param in a {@link java.lang.Object} object.
	 * @return a {@link java.lang.Object} object.
	 */
	Object map(Object in);
	
}
