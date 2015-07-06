package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public class ANull extends AOperation {

	private AAttribute attr;
	private boolean is;

	public ANull(AAttribute attr, boolean is) {
		this.attr = attr;
		this.is = is;
	}

	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		attr.print(query, buffer);
		buffer.append(" is ");
		if (!is) buffer.append("not ");
		buffer.append(" null");
	}

	@Override
	public void getAttributes(AttributeMap map) {
		attr.getAttributes(map);
	}

}
