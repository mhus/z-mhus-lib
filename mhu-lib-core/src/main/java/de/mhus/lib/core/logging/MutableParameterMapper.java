package de.mhus.lib.core.logging;

/**
 * <p>MutableParameterMapper interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public interface MutableParameterMapper {

	/**
	 * <p>clear.</p>
	 */
	void clear();
	/**
	 * <p>put.</p>
	 *
	 * @param clazz a {@link java.lang.String} object.
	 * @param mapper a {@link de.mhus.lib.core.logging.ParameterEntryMapper} object.
	 */
	void put(String clazz, ParameterEntryMapper mapper);
	
}
