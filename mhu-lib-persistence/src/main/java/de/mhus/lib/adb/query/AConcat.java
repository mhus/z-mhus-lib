package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public class AConcat extends AAttribute {

	private AAttribute[] parts;

	public AConcat(AAttribute ... parts) {
		this.parts = parts;
	}

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

	@Override
	public void getAttributes(AttributeMap map) {
		for (AAttribute part : parts)
			part.getAttributes(map);
	}

}
