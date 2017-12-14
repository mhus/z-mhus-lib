package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

/**
 * <p>ALiteralList class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ALiteralList extends APart {

	private APart[] operations;

	/**
	 * <p>Constructor for ALiteralList.</p>
	 *
	 * @param operations a {@link de.mhus.lib.adb.query.APart} object.
	 */
	public ALiteralList(APart ... operations) {
		this.operations = operations;
	}

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AttributeMap map) {
		for (APart part : operations)
			part.getAttributes(map);
	}

	public APart[] getOperations() {
		return operations;
	}

}
