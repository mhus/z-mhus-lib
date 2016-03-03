package de.mhus.lib.core.jmx;

import de.mhus.lib.annotations.jmx.JmxManaged;

/**
 * <p>JmxString class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@JmxManaged(descrition = "String Value")
public class JmxString extends MJmx {

	private String value;

	/**
	 * <p>Constructor for JmxString.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public JmxString(String name) {
		super(true,name);
	}
	
	/**
	 * <p>Setter for the field <code>value</code>.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.core.jmx.JmxString} object.
	 */
	public JmxString setValue(String  value) {
		this.value = value;
		return this;
	}
	
	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@JmxManaged
	public String getValue() {
		return value;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
