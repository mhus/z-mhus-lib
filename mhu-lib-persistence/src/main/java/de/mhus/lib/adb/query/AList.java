package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public class AList extends AAttribute {

	private AAttribute[] operations;

	public AList(AAttribute ... operations) {
		this.operations = operations;
	}

	@Override
	public void getAttributes(AttributeMap map) {
		for (AAttribute part : operations)
			part.getAttributes(map);
	}

	public AAttribute[] getOperations() {
		return operations;
	}

}
