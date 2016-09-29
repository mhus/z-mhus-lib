package de.mhus.lib.core.jmx;

import de.mhus.lib.annotations.jmx.JmxManaged;

/**
 * <p>JmxBoolean class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@JmxManaged(descrition = "Boolean Value")
public class JmxBoolean extends MJmx {

	private boolean value;

	/**
	 * <p>Constructor for JmxBoolean.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public JmxBoolean(String name) {
		super(true,name);
	}
	
	/**
	 * <p>Setter for the field <code>value</code>.</p>
	 *
	 * @param value a boolean.
	 * @return a {@link de.mhus.lib.core.jmx.JmxBoolean} object.
	 */
	public JmxBoolean setValue(boolean value) {
		this.value = value;
		return this;
	}
	
	/**
	 * <p>isValue.</p>
	 *
	 * @return a boolean.
	 */
	@JmxManaged
	public boolean isValue() {
		return value;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
