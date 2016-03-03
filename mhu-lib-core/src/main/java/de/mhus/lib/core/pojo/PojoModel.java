package de.mhus.lib.core.pojo;

/**
 * <p>PojoModel interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@SuppressWarnings("rawtypes")
public interface PojoModel extends Iterable<PojoAttribute> {

	/**
	 * <p>getManagedClass.</p>
	 *
	 * @return a {@link java.lang.Class} object.
	 */
	Class<?> getManagedClass();

	/**
	 * <p>getAttribute.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.core.pojo.PojoAttribute} object.
	 */
	PojoAttribute getAttribute(String name);

	/**
	 * <p>getAttributeNames.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	String[] getAttributeNames();

	/**
	 * <p>getAction.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.core.pojo.PojoAction} object.
	 */
	PojoAction getAction(String name);
	
	/**
	 * <p>getActionNames.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	String[] getActionNames();
	
}
