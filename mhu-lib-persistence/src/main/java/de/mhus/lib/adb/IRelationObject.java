package de.mhus.lib.adb;

import de.mhus.lib.adb.model.FieldRelation;
import de.mhus.lib.sql.DbConnection;

/**
 * <p>IRelationObject interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface IRelationObject {

	/**
	 * <p>prepareCreate.</p>
	 *
	 * @throws java.lang.Exception if any.
	 */
	void prepareCreate() throws Exception;

	/**
	 * <p>created.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @throws java.lang.Exception if any.
	 */
	void created(DbConnection con) throws Exception;

	/**
	 * <p>saved.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @throws java.lang.Exception if any.
	 */
	void saved(DbConnection con) throws Exception;

	/**
	 * <p>setManager.</p>
	 *
	 * @param fieldRelation a {@link de.mhus.lib.adb.model.FieldRelation} object.
	 * @param obj a {@link java.lang.Object} object.
	 */
	void setManager(FieldRelation fieldRelation, Object obj);

	/**
	 * <p>isChanged.</p>
	 *
	 * @return a boolean.
	 */
	boolean isChanged();

	/**
	 * <p>loaded.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 */
	void loaded(DbConnection con);

	/**
	 * <p>prepareSave.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @throws java.lang.Exception if any.
	 */
	void prepareSave(DbConnection con) throws Exception;

}
