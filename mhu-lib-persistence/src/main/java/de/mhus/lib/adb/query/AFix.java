package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public class AFix extends AAttribute {

	private String value;

	public AFix(String value) {
		this.value = value;
	}

	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append(value);
	}

	@Override
	public void getAttributes(AttributeMap map) {
	}

}
