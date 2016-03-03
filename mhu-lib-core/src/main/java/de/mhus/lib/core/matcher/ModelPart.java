package de.mhus.lib.core.matcher;

/**
 * <p>Abstract ModelPart class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public abstract class ModelPart {
	
	private boolean not;
	
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
	 * <p>matches.</p>
	 *
	 * @param str a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	protected abstract boolean matches(String str);
	
}
