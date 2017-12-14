package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public class AAnd extends APart {

	private APart[] operations;

	public AAnd(APart ... operations) {
		this.operations = operations;
	}

	@Override
	public void getAttributes(AttributeMap map) {
		for (APart part : operations)
			part.getAttributes(map);
	}

	public APart[] getOperations() {
		return operations;
	}

}
