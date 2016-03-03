package de.mhus.lib.form.definition;

/**
 * <p>FmValue class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FmValue {

	private Object value;
	
	/**
	 * <p>Constructor for FmValue.</p>
	 *
	 * @param value a {@link java.lang.Object} object.
	 */
	public FmValue(Object value) {
		this.value = value;
	}
	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getValue() {
		return value;
	}

}
