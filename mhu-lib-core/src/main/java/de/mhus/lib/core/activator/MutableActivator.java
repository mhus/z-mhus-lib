package de.mhus.lib.core.activator;

public interface MutableActivator {

	void addObject(Class<?> ifc, String name, Object obj);
	void addMap(Class<?> ifc, Class<?> clazz);
	void addMap(Class<?> ifc, String name, Class<?> clazz);
	void addMap(String name, Class<?> clazz);
	void removeMap(String name);
	void removeObject(String name);
	void removeObject(Class<?> ifc, String name);
	String[] getMapNames();
	String[] getObjectNames();

}
