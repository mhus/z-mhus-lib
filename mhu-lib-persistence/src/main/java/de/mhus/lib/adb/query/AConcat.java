package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

/**
 * <p>AConcat class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class AConcat extends AAttribute {

	private AAttribute[] parts;

	/**
	 * <p>Constructor for AConcat.</p>
	 *
	 * @param parts a {@link de.mhus.lib.adb.query.AAttribute} object.
	 */
	public AConcat(AAttribute ... parts) {
		this.parts = parts;
	}

	/** {@inheritDoc} */
	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append("concat(");
		boolean first = true;
		for (AAttribute part : parts) {
			if (first) first = false; else buffer.append(",");
			part.print(query, buffer);
		}
		buffer.append(")");
	}

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AttributeMap map) {
		for (AAttribute part : parts)
			part.getAttributes(map);
	}

}
