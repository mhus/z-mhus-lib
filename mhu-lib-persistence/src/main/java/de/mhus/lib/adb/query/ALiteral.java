package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

/**
 * <p>ALiteral class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ALiteral extends APart {

	private String literal;

	/**
	 * <p>Constructor for ALiteral.</p>
	 *
	 * @param literal a {@link java.lang.String} object.
	 */
	public ALiteral(String literal) {
		this.literal = literal;
	}

	/** {@inheritDoc} */
	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append(literal);
	}

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AttributeMap map) {
	}

}
