package de.mhus.lib.core.pojo;

@SuppressWarnings("rawtypes")
public interface PojoModel extends Iterable<PojoAttribute> {

	Class<?> getManagedClass();

	PojoAttribute getAttribute(String name);

	String[] getAttributeNames();

	PojoAction getAction(String name);
	
	String[] getActionNames();
	
}
