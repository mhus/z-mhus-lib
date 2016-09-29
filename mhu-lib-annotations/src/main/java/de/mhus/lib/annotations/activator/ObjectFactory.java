package de.mhus.lib.annotations.activator;

/**
 * <p>ObjectFactory interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface ObjectFactory {

	/**
	 * <p>create.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @param classes an array of {@link java.lang.Class} objects.
	 * @param objects an array of {@link java.lang.Object} objects.
	 * @return a {@link java.lang.Object} object.
	 */
	Object create(Class<?> clazz, Class<?>[] classes, Object[] objects);
}
