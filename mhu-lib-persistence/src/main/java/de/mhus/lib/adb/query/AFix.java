package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public class AFix extends AAttribute {

	private String value;

	public AFix(String value) {
		this.value = value;
	}

	@Override
	public void getAttributes(AttributeMap map) {
	}

	public String getValue() {
		return value;
	}

}
