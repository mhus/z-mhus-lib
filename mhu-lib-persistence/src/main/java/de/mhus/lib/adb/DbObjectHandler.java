package de.mhus.lib.adb;

import de.mhus.lib.errors.MException;
import de.mhus.lib.sql.DbConnection;

/**
 * <p>DbObjectHandler interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public interface DbObjectHandler {

	/**
	 * <p>saveObject.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param registryName a {@link java.lang.String} object.
	 * @param dbComfortableObject a {@link java.lang.Object} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	void saveObject(DbConnection con, String registryName, Object dbComfortableObject) throws MException;

	/**
	 * <p>objectChanged.</p>
	 *
	 * @param dbComfortableObject a {@link java.lang.Object} object.
	 * @return a boolean.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	boolean objectChanged(Object dbComfortableObject) throws MException;

	/**
	 * <p>reloadObject.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param registryName a {@link java.lang.String} object.
	 * @param dbComfortableObject a {@link java.lang.Object} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	void reloadObject(DbConnection con, String registryName, Object dbComfortableObject) throws MException;

	/**
	 * <p>deleteObject.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param registryName a {@link java.lang.String} object.
	 * @param dbComfortableObject a {@link java.lang.Object} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	void deleteObject(DbConnection con, String registryName, Object dbComfortableObject) throws MException;

	/**
	 * <p>createObject.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param dbComfortableObject a {@link java.lang.Object} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	void createObject(DbConnection con, Object dbComfortableObject) throws MException;

}
