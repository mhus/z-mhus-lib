package de.mhus.lib.core.pojo;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class FunctionAction implements PojoAction {

	private Class<?> clazz;
	private Method action;
	private String name;
	private FunctionAttribute<Object> parent;

	public FunctionAction(Class<?> clazz, Method action, String name,
			FunctionAttribute<Object> parent) {
		this.clazz = clazz;
		this.action = action;
		this.name = name;
		this.parent = parent;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object doExecute(Object pojo, Object... args) throws IOException {
		
		pojo = PojoParser.checkParent(parent, pojo);
		
		try {
			return action.invoke(pojo, args);
		} catch (Exception e) {
			throw new IOException("Execution of action [" + name + "] failed",e);
		}
	}
	
	@Override
	public Annotation getAnnotation(Class<? extends Annotation> annotationClass) {
		Annotation out = action.getAnnotation(annotationClass);
		return out;
	}

	@Override
	public Class<?> getManagedClass() {
		return clazz;
	}
	
	@Override
	public Class<?> getReturnType() {
		return action.getReturnType();
	}
	
	@Override
	public Class<?>[] getParameterType() {
		return action.getParameterTypes();
	}
}
