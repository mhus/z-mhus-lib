package de.mhus.lib.logging.parameter;

import de.mhus.lib.core.util.Stringifier;

/**
 * <p>StringifierParameterMapper class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class StringifierParameterMapper extends AbstractParameterMapper {

	/** {@inheritDoc} */
	@Override
	protected Object map(Object o) {
		Class<? extends Object> c = o.getClass();
		if (c.isPrimitive() || o instanceof String) return null;
		String cn = c.getName();
		if (cn.startsWith("java") || cn.startsWith("javax")) return null;
		return new Stringifier(o);
	}

}
