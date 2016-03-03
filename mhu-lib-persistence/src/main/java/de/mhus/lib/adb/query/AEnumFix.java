package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

/**
 * <p>AEnumFix class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class AEnumFix extends AAttribute {

	private Enum<?> value;

	/**
	 * <p>Constructor for AEnumFix.</p>
	 *
	 * @param value a {@link java.lang.Enum} object.
	 */
	public AEnumFix(Enum<?> value) {
		this.value = value;
	}

	/** {@inheritDoc} */
	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append(value.ordinal());
	}

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AttributeMap map) {
	}

}
