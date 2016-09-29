package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public class ANot extends APart {

	private APart operation;

	public ANot(APart operation) {
		this.operation = operation;
	}

	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append("not ");
		operation.print(query, buffer);
		//		buffer.append(')');
	}

	@Override
	public void getAttributes(AttributeMap map) {
		operation.getAttributes(map);
	}

}
