package de.mhus.lib.adb.query;

import java.util.UUID;

import de.mhus.lib.core.parser.AttributeMap;

public class ADynValue extends AAttribute {

	private String name;
	private Object value;

	public ADynValue(Object value) {
		this(UUID.randomUUID().toString(), value);
	}
	
	public ADynValue(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append('$').append(name).append('$');
	}

	@Override
	public void getAttributes(AttributeMap map) {
		map.put(name, value);
	}

}
