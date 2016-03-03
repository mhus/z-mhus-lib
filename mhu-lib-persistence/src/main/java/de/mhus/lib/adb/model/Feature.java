package de.mhus.lib.adb.model;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.errors.MException;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DbResult;

/**
 * <p>Abstract Feature class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class Feature extends MObject {

	protected DbManager manager;
	protected Table table;

	/**
	 * <p>init.</p>
	 *
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 * @param table a {@link de.mhus.lib.adb.model.Table} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void init(DbManager manager, Table table) throws MException {
		this.manager = manager;
		this.table = table;
		doInit();
	}

	/**
	 * <p>doInit.</p>
	 *
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	protected void doInit() throws MException {

	}

	/**
	 * <p>preCreateObject.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param object a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 * @since 3.2.9
	 */
	public void preCreateObject(DbConnection con, Object object) throws Exception {

	}

	/**
	 * <p>preSaveObject.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param object a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 * @since 3.2.9
	 */
	public void preSaveObject(DbConnection con, Object object) throws Exception {

	}

	/**
	 * <p>preGetObject.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param ret a {@link de.mhus.lib.sql.DbResult} object.
	 * @throws java.lang.Exception if any.
	 * @since 3.2.9
	 */
	public void preGetObject(DbConnection con, DbResult ret) throws Exception {

	}

	/**
	 * <p>postGetObject.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param obj a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 * @since 3.2.9
	 */
	public void postGetObject(DbConnection con, Object obj) throws Exception {

	}

	/**
	 * <p>preFillObject.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param res a {@link de.mhus.lib.sql.DbResult} object.
	 * @throws java.lang.Exception if any.
	 * @since 3.2.9
	 */
	public void preFillObject(Object obj, DbConnection con, DbResult res) throws Exception {

	}

	/**
	 * <p>deleteObject.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param object a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 */
	public void deleteObject(DbConnection con, Object object) throws Exception {

	}

	/**
	 * <p>getValue.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @param field a {@link de.mhus.lib.adb.model.Field} object.
	 * @param val a {@link java.lang.Object} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 * @since 3.2.9
	 */
	public Object getValue(Object obj, Field field, Object val) throws Exception {
		return val;
	}

	/**
	 * <p>setValue.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @param field a {@link de.mhus.lib.adb.model.Field} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 * @since 3.2.9
	 */
	public Object setValue(Object obj, Field field, Object value) throws Exception {
		return value;
	}

	/**
	 * <p>postFillObject.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @throws java.lang.Exception if any.
	 * @since 3.2.9
	 */
	public void postFillObject(Object obj, DbConnection con) throws Exception {
	}

	/**
	 * <p>postCreateObject.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param object a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 * @since 3.2.9
	 */
	public void postCreateObject(DbConnection con, Object object) throws Exception {
	}

	/**
	 * <p>postSaveObject.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param object a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 * @since 3.2.9
	 */
	public void postSaveObject(DbConnection con, Object object) throws Exception {
	}

}
