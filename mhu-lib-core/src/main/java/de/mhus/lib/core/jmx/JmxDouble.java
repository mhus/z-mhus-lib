package de.mhus.lib.core.jmx;

import de.mhus.lib.annotations.jmx.JmxManaged;

@JmxManaged(descrition = "Double Value")
public class JmxDouble extends MJmx {

	private double value;

	public JmxDouble(String name) {
		super(true,name);
	}
	
	public JmxDouble setValue(double value) {
		this.value = value;
		return this;
	}
	
	@JmxManaged
	public double getValue() {
		return value;
	}

	public String toString() {
		return String.valueOf(value);
	}
	
}
