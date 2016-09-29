package de.mhus.lib.core.matcher;

import java.util.Map;

/**
 * <p>NullPattern class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class NullPattern extends ModelPattern {

	/** {@inheritDoc} */
	@Override
	public void setPattern(String pattern) {
		
	}

	/** {@inheritDoc} */
	@Override
	public String getPattern() {
		return "null";
	}

	/** {@inheritDoc} */
	@Override
	public String getPatternTypeName() {
		return "null";
	}

	/** {@inheritDoc} */
	@Override
	protected boolean matches(String str) {
		return str == null;
	}

	/** {@inheritDoc} */
	protected boolean matches(Map<String,?> map) {
		Object val = map.get(getParamName());
		return val == null;
	}

}
