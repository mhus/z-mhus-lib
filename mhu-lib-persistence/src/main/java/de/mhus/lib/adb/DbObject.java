package de.mhus.lib.adb;

import de.mhus.lib.sql.DbConnection;

/**
 * Interface to allow send hooks to the object.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface DbObject extends Persistable {

	/**
	 * <p>doPreCreate.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 */
	void doPreCreate(DbConnection con);

	/**
	 * <p>doPreSave.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 */
	void doPreSave(DbConnection con);

	/**
	 * <p>doInit.</p>
	 *
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 * @param registryName a {@link java.lang.String} object.
	 * @param isPersistent a boolean.
	 */
	void doInit(DbManager manager, String registryName, boolean isPersistent);

	/**
	 * <p>doPreDelete.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 */
	void doPreDelete(DbConnection con);

	/**
	 * <p>doPostLoad.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 */
	void doPostLoad(DbConnection con);

	/**
	 * <p>doPostCreate.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 */
	void doPostCreate(DbConnection con);

	/**
	 * <p>doPostDelete.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 */
	void doPostDelete(DbConnection con);

	/**
	 * <p>isAdbPersistent.</p>
	 *
	 * @return a boolean.
	 */
	boolean isAdbPersistent();

	/**
	 * <p>getDbManager.</p>
	 *
	 * @return a {@link de.mhus.lib.adb.DbManager} object.
	 * @since 3.2.9
	 */
	DbManager getDbManager();
}
