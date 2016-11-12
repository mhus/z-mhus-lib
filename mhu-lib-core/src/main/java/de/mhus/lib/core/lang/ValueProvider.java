package de.mhus.lib.core.lang;

public interface ValueProvider<E> {

	E getValue() throws Exception;
	
}
