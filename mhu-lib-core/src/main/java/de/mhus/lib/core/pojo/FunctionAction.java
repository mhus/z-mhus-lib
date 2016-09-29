package de.mhus.lib.core.pojo;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * <p>FunctionAction class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FunctionAction implements PojoAction {

	private Class<?> clazz;
	private Method action;
	private String name;
	private FunctionAttribute<Object> parent;

	/**
	 * <p>Constructor for FunctionAction.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @param action a {@link java.lang.reflect.Method} object.
	 * @param name a {@link java.lang.String} object.
	 * @param parent a {@link de.mhus.lib.core.pojo.FunctionAttribute} object.
	 */
	public FunctionAction(Class<?> clazz, Method action, String name,
			FunctionAttribute<Object> parent) {
		this.clazz = clazz;
		this.action = action;
		this.name = name;
		this.parent = parent;
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return name;
	}

	/** {@inheritDoc} */
	@Override
	public Object doExecute(Object pojo, Object... args) throws IOException {
		
		pojo = PojoParser.checkParent(parent, pojo);
		
		try {
			return action.invoke(pojo, args);
		} catch (Exception e) {
			throw new IOException("Execution of action [" + name + "] failed",e);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public Annotation getAnnotation(Class<? extends Annotation> annotationClass) {
		
		
		Annotation out = action.getAnnotation(annotationClass);
		if (out != null) return out;
		
		Set<Method> res = MethodAnalyser.getMethodsForMethod(clazz, action.getName());
		for (Method m2 : res) {
			out = m2.getAnnotation(annotationClass);
			if (out != null) return out;
		}
		
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public Class<?> getManagedClass() {
		return clazz;
	}
	
	/** {@inheritDoc} */
	@Override
	public Class<?> getReturnType() {
		return action.getReturnType();
	}
	
	/** {@inheritDoc} */
	@Override
	public Class<?>[] getParameterType() {
		return action.getParameterTypes();
	}
}
