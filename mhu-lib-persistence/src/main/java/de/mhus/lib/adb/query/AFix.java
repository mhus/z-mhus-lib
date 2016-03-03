package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

/**
 * <p>AFix class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class AFix extends AAttribute {

	private String value;

	/**
	 * <p>Constructor for AFix.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 */
	public AFix(String value) {
		this.value = value;
	}

	/** {@inheritDoc} */
	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append(value);
	}

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AttributeMap map) {
	}

}
