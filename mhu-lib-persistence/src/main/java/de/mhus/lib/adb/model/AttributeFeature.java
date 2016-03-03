package de.mhus.lib.adb.model;

import de.mhus.lib.adb.DbManager;

/**
 * <p>AttributeFeature interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface AttributeFeature {

	/**
	 * <p>init.</p>
	 *
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 * @param field a {@link de.mhus.lib.adb.model.Field} object.
	 */
	void init(DbManager manager, Field field);

	/**
	 * <p>set.</p>
	 *
	 * @param pojo a {@link java.lang.Object} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link java.lang.Object} object.
	 */
	Object set(Object pojo, Object value);

	/**
	 * <p>get.</p>
	 *
	 * @param pojo a {@link java.lang.Object} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link java.lang.Object} object.
	 */
	Object get(Object pojo, Object value);

}
