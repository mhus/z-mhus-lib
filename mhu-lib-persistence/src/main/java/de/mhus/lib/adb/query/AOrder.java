package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public class AOrder extends AOperation {

	private String attribute;
	private Class<?> clazz;
	private boolean asc;

	public AOrder(Class<?> clazz, String attribute, boolean asc) {
		this.clazz = clazz;
		this.attribute = attribute.toLowerCase();
		this.asc = asc;
	}

	@Override
	public void getAttributes(AttributeMap map) {
	}

	public String getAttribute() {
		return attribute;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public boolean isAsc() {
		return asc;
	}

}
