package de.mhus.lib.core.pojo;

import java.lang.annotation.Annotation;

public interface PojoAction {

	String getName();
	
	Object doExecute(Object pojo, Object ... args) throws Exception;

	Annotation getAnnotation(Class<? extends Annotation> annotationClass);

	Class<?> getManagedClass();

	Class<?> getReturnType();

	Class<?>[] getParameterType();
	

}
