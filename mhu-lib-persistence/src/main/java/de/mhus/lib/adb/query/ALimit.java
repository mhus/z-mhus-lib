package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public class ALimit extends AOperation {

	private int limit;

	public ALimit(int limit) {
		this.limit = limit;
	}

	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append(" LIMIT 0,").append(limit); //mysql specific !!
	}

	@Override
	public void getAttributes(AttributeMap map) {
	}

}
