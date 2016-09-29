package de.mhus.lib.core.matcher;

import java.util.Map;

/**
 * <p>Abstract ModelPart class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class ModelPart {
	
	private boolean not;
	private String param;
	
	/**
	 * <p>isNot.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isNot() {
		return not;
	}

	/**
	 * <p>Setter for the field <code>not</code>.</p>
	 *
	 * @param not a boolean.
	 */
	public void setNot(boolean not) {
		this.not = not;
	}
	
	/**
	 * <p>m.</p>
	 *
	 * @param str a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean m(String str) {
		if (not)
			return !matches(str);
		else
			return matches(str);
	}

	/**
	 * <p>m.</p>
	 *
	 * @param map a {@link java.util.Map} object.
	 * @return a boolean.
	 * @since 3.3.0
	 */
	public boolean m(Map<String,?> map) {
		if (not)
			return !matches(map);
		else
			return matches(map);
	}

	/**
	 * <p>setParamName.</p>
	 *
	 * @param param a {@link java.lang.String} object.
	 * @since 3.3.0
	 */
	public void setParamName(String param) {
		this.param = param;
	}
	
	/**
	 * <p>getParamName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 * @since 3.3.0
	 */
	public String getParamName() {
		return param;
	}

	/**
	 * <p>matches.</p>
	 *
	 * @param map a {@link java.util.Map} object.
	 * @return a boolean.
	 * @since 3.3.0
	 */
	protected boolean matches(Map<String,?> map) {
		Object val = map.get(param);
		if (val != null)
			return matches(String.valueOf(val));
		return false;
	}
	
	/**
	 * <p>matches.</p>
	 *
	 * @param str a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	protected abstract boolean matches(String str);
	
}
