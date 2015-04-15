package de.mhus.lib.core.jmx;

import de.mhus.lib.annotations.jmx.JmxManaged;

@JmxManaged(descrition = "Float Value")
public class JmxFloat extends MJmx {

	private float value;

	public JmxFloat(String name) {
		super(true,name);
	}
	
	public JmxFloat setValue(float value) {
		this.value = value;
		return this;
	}
	
	@JmxManaged
	public float getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
}
