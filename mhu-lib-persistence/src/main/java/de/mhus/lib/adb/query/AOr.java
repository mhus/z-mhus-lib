package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public class AOr extends APart {

	private APart[] operations;

	public AOr(APart ... operations) {
		this.operations = operations;
	}

	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append('(');
		boolean first = true;
		for (APart part : operations) {
			if (first)
				first = false;
			else
				buffer.append(" or ");
			part.print(query, buffer);
		}
		buffer.append(')');
	}

	@Override
	public void getAttributes(AttributeMap map) {
		for (APart part : operations)
			part.getAttributes(map);
	}

}
