package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public class AList extends AAttribute {

	private AAttribute[] operations;

	public AList(AAttribute ... operations) {
		this.operations = operations;
	}

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
	
	@Override
	public void getAttributes(AttributeMap map) {
		for (AAttribute part : operations)
			part.getAttributes(map);
	}

}
