package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public class ALiteralList extends APart {

	private APart[] operations;

	public ALiteralList(APart ... operations) {
		this.operations = operations;
	}

	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		for (APart part : operations) {
			part.print(query, buffer);
		}
	}

	@Override
	public void getAttributes(AttributeMap map) {
		for (APart part : operations)
			part.getAttributes(map);
	}

}
