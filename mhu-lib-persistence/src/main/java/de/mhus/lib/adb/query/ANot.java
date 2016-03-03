package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

/**
 * <p>ANot class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ANot extends APart {

	private APart operation;

	/**
	 * <p>Constructor for ANot.</p>
	 *
	 * @param operation a {@link de.mhus.lib.adb.query.APart} object.
	 */
	public ANot(APart operation) {
		this.operation = operation;
	}

	/** {@inheritDoc} */
	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append("not ");
		operation.print(query, buffer);
		//		buffer.append(')');
	}

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AttributeMap map) {
		operation.getAttributes(map);
	}

}
