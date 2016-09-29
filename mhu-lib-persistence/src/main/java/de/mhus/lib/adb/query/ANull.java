package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

/**
 * <p>ANull class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ANull extends APart {

	private AAttribute attr;
	private boolean is;

	/**
	 * <p>Constructor for ANull.</p>
	 *
	 * @param attr a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @param is a boolean.
	 */
	public ANull(AAttribute attr, boolean is) {
		this.attr = attr;
		this.is = is;
	}

	/** {@inheritDoc} */
	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		attr.print(query, buffer);
		buffer.append(" is ");
		if (!is) buffer.append("not ");
		buffer.append("null");
	}

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AttributeMap map) {
		attr.getAttributes(map);
	}

}
