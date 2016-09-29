package de.mhus.lib.core.jmx;

import de.mhus.lib.annotations.jmx.JmxManaged;

/**
 * <p>JmxInteger class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@JmxManaged(descrition = "Integer Value")
public class JmxInteger extends MJmx {

	private int value;

	/**
	 * <p>Constructor for JmxInteger.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public JmxInteger(String name) {
		super(true,name);
	}
	
	/**
	 * <p>Setter for the field <code>value</code>.</p>
	 *
	 * @param value a int.
	 * @return a {@link de.mhus.lib.core.jmx.JmxInteger} object.
	 */
	public JmxInteger setValue(int value) {
		this.value = value;
		return this;
	}
	
	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return a int.
	 */
	@JmxManaged
	public int getValue() {
		return value;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
