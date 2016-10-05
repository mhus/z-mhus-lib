package de.mhus.lib.core.logging;

public interface MutableParameterMapper {

	void clear();
	void put(String clazz, ParameterEntryMapper mapper);
	
}
