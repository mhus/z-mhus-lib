package de.mhus.lib.core.pojo;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * <p>PojoAttribute interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface PojoAttribute<T> {

	/**
	 * <p>get.</p>
	 *
	 * @param pojo a {@link java.lang.Object} object.
	 * @return a T object.
	 * @throws java.io.IOException if any.
	 */
	T get(Object pojo) throws IOException;

	/**
	 * <p>set.</p>
	 *
	 * @param pojo a {@link java.lang.Object} object.
	 * @param value a T object.
	 * @throws java.io.IOException if any.
	 */
	void set(Object pojo, T value) throws IOException;

	/**
	 * <p>getType.</p>
	 *
	 * @return a {@link java.lang.Class} object.
	 */
	Class<?> getType();

	/**
	 * <p>canRead.</p>
	 *
	 * @return a boolean.
	 */
	boolean canRead();

	/**
	 * <p>canWrite.</p>
	 *
	 * @return a boolean.
	 */
	boolean canWrite();
	
	/**
	 * <p>getManagedClass.</p>
	 *
	 * @return a {@link java.lang.Class} object.
	 */
	Class<T> getManagedClass();

	/**
	 * <p>getName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String getName();

	/**
	 * <p>getAnnotation.</p>
	 *
	 * @param annotationClass a {@link java.lang.Class} object.
	 * @param <A> a A object.
	 * @return a A object.
	 */
	<A extends Annotation> A getAnnotation(Class<? extends A> annotationClass);
	
}
