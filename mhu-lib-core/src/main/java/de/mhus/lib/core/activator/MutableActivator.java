package de.mhus.lib.core.activator;

/**
 * <p>MutableActivator interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface MutableActivator {

	/**
	 * <p>addObject.</p>
	 *
	 * @param ifc a {@link java.lang.Class} object.
	 * @param name a {@link java.lang.String} object.
	 * @param obj a {@link java.lang.Object} object.
	 */
	void addObject(Class<?> ifc, String name, Object obj);
	/**
	 * <p>addMap.</p>
	 *
	 * @param ifc a {@link java.lang.Class} object.
	 * @param clazz a {@link java.lang.Class} object.
	 */
	void addMap(Class<?> ifc, Class<?> clazz);
	/**
	 * <p>addMap.</p>
	 *
	 * @param ifc a {@link java.lang.Class} object.
	 * @param name a {@link java.lang.String} object.
	 * @param clazz a {@link java.lang.Class} object.
	 */
	void addMap(Class<?> ifc, String name, Class<?> clazz);
	/**
	 * <p>addMap.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param clazz a {@link java.lang.Class} object.
	 */
	void addMap(String name, Class<?> clazz);
	/**
	 * <p>removeMap.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	void removeMap(String name);
	/**
	 * <p>removeObject.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	void removeObject(String name);
	/**
	 * <p>removeObject.</p>
	 *
	 * @param ifc a {@link java.lang.Class} object.
	 * @param name a {@link java.lang.String} object.
	 */
	void removeObject(Class<?> ifc, String name);
	/**
	 * <p>getMapNames.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	String[] getMapNames();
	/**
	 * <p>getObjectNames.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	String[] getObjectNames();

}
