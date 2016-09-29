package de.mhus.lib.core.jmx;

import de.mhus.lib.annotations.jmx.JmxManaged;

/**
 * <p>JmxLong class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@JmxManaged(descrition = "Long Value")
public class JmxLong extends MJmx {

	private long value;

	/**
	 * <p>Constructor for JmxLong.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public JmxLong(String name) {
		super(true,name);
	}
	
	/**
	 * <p>Setter for the field <code>value</code>.</p>
	 *
	 * @param value a long.
	 * @return a {@link de.mhus.lib.core.jmx.JmxLong} object.
	 */
	public JmxLong setValue(long value) {
		this.value = value;
		return this;
	}
	
	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return a long.
	 */
	@JmxManaged
	public long getValue() {
		return value;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
}
