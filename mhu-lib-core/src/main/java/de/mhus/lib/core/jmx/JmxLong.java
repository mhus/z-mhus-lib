package de.mhus.lib.core.jmx;

import de.mhus.lib.annotations.jmx.JmxManaged;

@JmxManaged(descrition = "Long Value")
public class JmxLong extends MJmx {

	private long value;

	public JmxLong(String name) {
		super(true,name);
	}
	
	public JmxLong setValue(long value) {
		this.value = value;
		return this;
	}
	
	@JmxManaged
	public long getValue() {
		return value;
	}

	public String toString() {
		return String.valueOf(value);
	}
	
}
