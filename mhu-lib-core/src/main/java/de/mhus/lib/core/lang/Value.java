package de.mhus.lib.core.lang;

import de.mhus.lib.basics.Valueable;
import de.mhus.lib.core.MSystem;

public class Value<T> implements Valueable<T> {

	public Value() {}
	
	public Value(T initial) {
		value = initial;
	}
	
	public T value;
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
	@Override
	public boolean equals(Object in) {
		if (in != null && in instanceof Valueable)
			return MSystem.equals(value, ((Valueable<?>)in).getValue() );
		return MSystem.equals(value, in);
	}

	@Override
	public T getValue() {
		return value;
	}
	
}
