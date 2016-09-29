package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public class AEnumFix extends AAttribute {

	private Enum<?> value;

	public AEnumFix(Enum<?> value) {
		this.value = value;
	}

	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append(value.ordinal());
	}

	@Override
	public void getAttributes(AttributeMap map) {
	}

}
