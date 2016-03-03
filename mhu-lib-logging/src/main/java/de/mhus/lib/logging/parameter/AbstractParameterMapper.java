package de.mhus.lib.logging.parameter;

import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.ParameterMapper;

/**
 * <p>Abstract AbstractParameterMapper class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public abstract class AbstractParameterMapper implements ParameterMapper {

	/** {@inheritDoc} */
	@Override
	public Object[] map(Log log, Object[] msg) {
		if (msg == null) return msg;
		for (int i = 0; i < msg.length; i++) {
			Object o = msg[i];
			if (o == null) continue;
			o = map(o);
			if (o != null) msg[i] = o;
		}
		return msg;
	}

	/**
	 * <p>map.</p>
	 *
	 * @param o a {@link java.lang.Object} object.
	 * @return a {@link java.lang.Object} object.
	 */
	protected abstract Object map(Object o);
	
}
