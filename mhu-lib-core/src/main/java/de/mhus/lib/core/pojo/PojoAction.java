package de.mhus.lib.core.pojo;

import java.lang.annotation.Annotation;

/**
 * <p>PojoAction interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface PojoAction {

	/**
	 * <p>getName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String getName();
	
	/**
	 * <p>doExecute.</p>
	 *
	 * @param pojo a {@link java.lang.Object} object.
	 * @param args a {@link java.lang.Object} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 */
	Object doExecute(Object pojo, Object ... args) throws Exception;

	/**
	 * <p>getAnnotation.</p>
	 *
	 * @param annotationClass a {@link java.lang.Class} object.
	 * @return a {@link java.lang.annotation.Annotation} object.
	 */
	Annotation getAnnotation(Class<? extends Annotation> annotationClass);

	/**
	 * <p>getManagedClass.</p>
	 *
	 * @return a {@link java.lang.Class} object.
	 */
	Class<?> getManagedClass();

	/**
	 * <p>getReturnType.</p>
	 *
	 * @return a {@link java.lang.Class} object.
	 */
	Class<?> getReturnType();

	/**
	 * <p>getParameterType.</p>
	 *
	 * @return an array of {@link java.lang.Class} objects.
	 */
	Class<?>[] getParameterType();
	

}
