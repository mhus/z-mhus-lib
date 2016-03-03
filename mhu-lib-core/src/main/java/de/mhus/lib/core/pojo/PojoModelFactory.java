package de.mhus.lib.core.pojo;

/**
 * <p>PojoModelFactory interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public interface PojoModelFactory {
	
	/**
	 * <p>createPojoModel.</p>
	 *
	 * @param pojoClass a {@link java.lang.Class} object.
	 * @return a {@link de.mhus.lib.core.pojo.PojoModel} object.
	 */
	PojoModel createPojoModel(Class<?> pojoClass);
	
}
