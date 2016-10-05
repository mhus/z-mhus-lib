package de.mhus.lib.core.cast;

public class FloatToString implements Caster<Float, String> {

	@Override
	public Class<? extends String> getToClass() {
		return String.class;
	}

	@Override
	public Class<? extends Float> getFromClass() {
		return Float.class;
	}

	@Override
	public String cast(Float in, String def) {
		return toString(in);
	}

	public String toString(float _in) {
		return Float.toString(_in).replace(",", ".");
	}
	
}
