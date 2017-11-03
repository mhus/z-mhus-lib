package de.mhus.lib.core.definition;

import de.mhus.lib.core.MCast;
import de.mhus.lib.errors.MException;

public class DefAttribute implements IDefAttribute {

	private String name;
	private Object value;

	public DefAttribute(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public void inject(DefComponent parent) throws MException {
		parent.setString(name, MCast.objectToString(value) );
	}

	public String getName() {
		return name;
	}
	
	public Object getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return name + "=" + value;
	}
	
}
