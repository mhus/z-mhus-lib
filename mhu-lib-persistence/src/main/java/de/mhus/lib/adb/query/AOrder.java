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
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append("$db.").append(query.getManager().getMappingName(clazz)).append('.').append(attribute).append('$');
		buffer.append(' ').append(asc ? "ASC" : "DESC");
	}

	@Override
	public void getAttributes(AttributeMap map) {
	}

}
