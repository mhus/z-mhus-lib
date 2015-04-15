package de.mhus.lib.core.jmx;

import de.mhus.lib.annotations.jmx.JmxManaged;

@JmxManaged(descrition = "Integer Value")
public class JmxInteger extends MJmx {

	private int value;

	public JmxInteger(String name) {
		super(true,name);
	}
	
	public JmxInteger setValue(int value) {
		this.value = value;
		return this;
	}
	
	@JmxManaged
	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
