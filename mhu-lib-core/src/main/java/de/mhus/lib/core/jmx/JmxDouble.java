package de.mhus.lib.core.jmx;

import de.mhus.lib.annotations.jmx.JmxManaged;

/**
 * <p>JmxDouble class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@JmxManaged(descrition = "Double Value")
public class JmxDouble extends MJmx {

	private double value;

	/**
	 * <p>Constructor for JmxDouble.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public JmxDouble(String name) {
		super(true,name);
	}
	
	/**
	 * <p>Setter for the field <code>value</code>.</p>
	 *
	 * @param value a double.
	 * @return a {@link de.mhus.lib.core.jmx.JmxDouble} object.
	 */
	public JmxDouble setValue(double value) {
		this.value = value;
		return this;
	}
	
	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return a double.
	 */
	@JmxManaged
	public double getValue() {
		return value;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
}
