package de.mhus.lib.adb.query;

import java.util.UUID;

import de.mhus.lib.core.parser.AttributeMap;

/**
 * <p>ADynValue class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ADynValue extends AAttribute {

	private String name;
	private Object value;

	/**
	 * <p>Constructor for ADynValue.</p>
	 *
	 * @param value a {@link java.lang.Object} object.
	 */
	public ADynValue(Object value) {
		this(UUID.randomUUID().toString(), value);
	}

	/**
	 * <p>Constructor for ADynValue.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 */
	public ADynValue(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	/** {@inheritDoc} */
	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append('$').append(name).append('$');
	}

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AttributeMap map) {
		map.put(name, value);
	}
	
	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * <p>Setter for the field <code>value</code>.</p>
	 *
	 * @param value a {@link java.lang.Object} object.
	 */
	public void setValue(Object value) {
		this.value = value;
	}
}
