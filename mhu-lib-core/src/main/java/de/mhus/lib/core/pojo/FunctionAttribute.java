package de.mhus.lib.core.pojo;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import de.mhus.lib.core.MCast;

/**
 * <p>FunctionAttribute class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FunctionAttribute<T> implements PojoAttribute<T> {

	private Class<T> clazz;
	private Method getter;
	private Method setter;
	private String name;
	private FunctionAttribute<Object> parent;

	/**
	 * <p>Constructor for FunctionAttribute.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @param getter a {@link java.lang.reflect.Method} object.
	 * @param setter a {@link java.lang.reflect.Method} object.
	 * @param name a {@link java.lang.String} object.
	 * @param parent a {@link de.mhus.lib.core.pojo.FunctionAttribute} object.
	 */
	public FunctionAttribute(Class<T> clazz, Method getter, Method setter, String name, FunctionAttribute<Object> parent) {
		this.clazz = clazz;
		this.getter = getter;
		this.setter = setter;
		this.name = name;
		this.parent = parent;
	}

	/** {@inheritDoc} */
	@Override
	public Class<T> getManagedClass() {
		return clazz;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean canRead() {
		return getter != null;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean canWrite() {
		return setter != null;
	}
	
	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getType() {
		if (getter != null)
			return (Class<T>) getter.getReturnType();
		else
			return (Class<T>) setter.getParameterTypes()[0];
	}
	
	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public void set(Object pojo, T value) throws IOException {
		
		pojo = PojoParser.checkParent(parent,pojo);
		
		if (setter == null)
			throw new IOException("Method is read only: " + getter.getName());
//		if (!getType().isInstance(pojo))
//			throw new IOException("Object is not instance of " + getType().getCanonicalName());
		try {
			value = (T) MCast.toType(value, getType(), null);
			if (getType().isPrimitive() && value == null) {
				// that's not possible
				value = (T)MCast.getDefaultPrimitive(getType());
			}
			setter.invoke(pojo, value);
		} catch (Exception e) {
			throw new IOException("Error set: " + name + "(" + getType() + ")=" + value, e);
		}
	}
	
	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public T get(Object pojo) throws IOException {
		
		pojo = PojoParser.checkParent(parent,pojo);

//		if (!getType().isInstance(pojo))
//			throw new IOException("Object is not instance of " + getType().getCanonicalName());
		try {
			return (T) getter.invoke(pojo);
		} catch (Exception e) {
			throw new IOException("Error get: " + name + "(" + clazz + ")", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return name;
	}

	/** {@inheritDoc} */
	@Override
	public <A extends Annotation> A getAnnotation(Class<? extends A> annotationClass) {
		A out = getter == null ? null : getter.getAnnotation(annotationClass);
		if (out == null && setter != null)
			out = setter.getAnnotation(annotationClass);
		return out;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "[" + name + "@FunctionAttribute]";
	}
}
