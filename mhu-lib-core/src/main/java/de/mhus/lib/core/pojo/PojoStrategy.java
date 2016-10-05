package de.mhus.lib.core.pojo;

public interface PojoStrategy {

	
	void parseObject(PojoParser parser, Object pojo, PojoModelImpl model);
	
	void parse(PojoParser parser, Class<?> clazz, PojoModelImpl model);
	
}
