package de.mhus.lib.annotations.activator;

public interface ObjectFactory {

	Object create(Class<?> clazz, Class<?>[] classes, Object[] objects);
}
