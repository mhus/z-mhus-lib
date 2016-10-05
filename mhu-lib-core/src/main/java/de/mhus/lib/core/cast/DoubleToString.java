package de.mhus.lib.core.cast;

import java.text.DecimalFormat;

public class DoubleToString implements Caster<Double, String> {

	private static DecimalFormat doubleFormat = new DecimalFormat("0.##########");

	@Override
	public Class<? extends String> getToClass() {
		return String.class;
	}

	@Override
	public Class<? extends Double> getFromClass() {
		return Double.class;
	}

	@Override
	public String cast(Double in, String def) {
		return null;
	}

	public String toString(double _in) {
		String out = doubleFormat.format(_in);
		if (out.indexOf(',') >= 0) out = out.replace(",", "."); // for secure
		return out;
	}
	
}
