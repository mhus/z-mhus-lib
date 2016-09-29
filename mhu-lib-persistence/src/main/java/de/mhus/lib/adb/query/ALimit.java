package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public class ALimit extends AOperation {

	private int limit;
	private int offset;

	public ALimit(int limit) {
		this(0,limit);
	}
	
	public ALimit(int offset, int limit) {
		this.offset = offset;
		this.limit = limit;
	}

	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append(" LIMIT ").append(offset).append(",").append(limit); //mysql specific !!
	}

	@Override
	public void getAttributes(AttributeMap map) {
	}

}
