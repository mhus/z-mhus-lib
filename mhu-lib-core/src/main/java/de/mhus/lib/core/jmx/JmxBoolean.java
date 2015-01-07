package de.mhus.lib.core.jmx;

import de.mhus.lib.annotations.jmx.JmxManaged;

@JmxManaged(descrition = "Boolean Value")
public class JmxBoolean extends MJmx {

	private boolean value;

	public JmxBoolean(String name) {
		super(true,name);
	}
	
	public JmxBoolean setValue(boolean value) {
		this.value = value;
		return this;
	}
	
	@JmxManaged
	public boolean isValue() {
		return value;
	}

	public String toString() {
		return String.valueOf(value);
	}

}
