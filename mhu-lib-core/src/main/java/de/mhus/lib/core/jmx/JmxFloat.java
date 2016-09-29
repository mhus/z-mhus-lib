package de.mhus.lib.core.jmx;

import de.mhus.lib.annotations.jmx.JmxManaged;

/**
 * <p>JmxFloat class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@JmxManaged(descrition = "Float Value")
public class JmxFloat extends MJmx {

	private float value;

	/**
	 * <p>Constructor for JmxFloat.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public JmxFloat(String name) {
		super(true,name);
	}
	
	/**
	 * <p>Setter for the field <code>value</code>.</p>
	 *
	 * @param value a float.
	 * @return a {@link de.mhus.lib.core.jmx.JmxFloat} object.
	 */
	public JmxFloat setValue(float value) {
		this.value = value;
		return this;
	}
	
	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return a float.
	 */
	@JmxManaged
	public float getValue() {
		return value;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
}
