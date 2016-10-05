package de.mhus.lib.core.jmx;

import de.mhus.lib.annotations.jmx.JmxManaged;

@JmxManaged(descrition = "String Value")
public class JmxString extends MJmx {

	private String value;

	public JmxString(String name) {
		super(true,name);
	}
	
	public JmxString setValue(String  value) {
		this.value = value;
		return this;
	}
	
	@JmxManaged
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
