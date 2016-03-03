package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

/**
 * <p>AList class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class AList extends AAttribute {

	private AAttribute[] operations;

	/**
	 * <p>Constructor for AList.</p>
	 *
	 * @param operations a {@link de.mhus.lib.adb.query.AAttribute} object.
	 */
	public AList(AAttribute ... operations) {
		this.operations = operations;
	}

	/** {@inheritDoc} */
	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append('(');
		boolean first = true;
		for (AAttribute part : operations) {
			if (first)
				first = false;
			else
				buffer.append(",");
			part.print(query, buffer);
		}
		buffer.append(')');
	}

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AttributeMap map) {
		for (AAttribute part : operations)
			part.getAttributes(map);
	}

}
