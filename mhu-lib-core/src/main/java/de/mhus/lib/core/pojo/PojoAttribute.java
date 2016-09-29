package de.mhus.lib.core.pojo;

import java.io.IOException;
import java.lang.annotation.Annotation;

public interface PojoAttribute<T> {

	T get(Object pojo) throws IOException;

	void set(Object pojo, T value) throws IOException;

	Class<?> getType();

	boolean canRead();

	boolean canWrite();
	
	Class<T> getManagedClass();

	String getName();

	<A extends Annotation> A getAnnotation(Class<? extends A> annotationClass);
	
}
