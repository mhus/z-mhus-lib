package de.mhus.lib.core.cast;

public interface Caster<F,T> {

	public Class<? extends T> getToClass();

	public Class<? extends F> getFromClass();
		
	public T cast(F in, T def);
	
}
