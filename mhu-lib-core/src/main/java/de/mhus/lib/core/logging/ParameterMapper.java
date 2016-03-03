package de.mhus.lib.core.logging;

/**
 * <p>ParameterMapper interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public interface ParameterMapper {

	/**
	 * <p>map.</p>
	 *
	 * @param log a {@link de.mhus.lib.core.logging.Log} object.
	 * @param msg an array of {@link java.lang.Object} objects.
	 * @return an array of {@link java.lang.Object} objects.
	 */
	public Object[] map(Log log, Object[] msg);

}
