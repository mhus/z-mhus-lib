package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

/**
 * <p>AAnd class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class AAnd extends APart {

	private APart[] operations;

	/**
	 * <p>Constructor for AAnd.</p>
	 *
	 * @param operations a {@link de.mhus.lib.adb.query.APart} object.
	 */
	public AAnd(APart ... operations) {
		this.operations = operations;
	}

	/** {@inheritDoc} */
	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append('(');
		boolean first = true;
		for (APart part : operations) {
			if (first)
				first = false;
			else
				buffer.append(" and ");
			part.print(query, buffer);
		}
		buffer.append(')');
	}

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AttributeMap map) {
		for (APart part : operations)
			part.getAttributes(map);
	}

}
