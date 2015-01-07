package de.mhus.lib.adb;

import de.mhus.lib.adb.model.Table;
import de.mhus.lib.errors.AccessDeniedException;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DbResult;

/**
 * Skeleton to allow the validation of access rights. It's created in DbSchema.
 * 
 * @author mikehummel
 *
 */
public abstract class DbAccessManager {

	public static final String FEATURE_NAME = "accesscontrol";

	/**
	 * Throws an Exception if the access is not allowed.
	 * 
	 * @param manager
	 * @param c
	 * @param con
	 * @param object
	 * @param right
	 * @throws AccessDeniedException
	 */
	public abstract void hasAccess(DbManager manager, Table c, DbConnection con, Object object, int right) throws AccessDeniedException;
	
	/**
	 * This is used to validate rights before reading from a database. The right to check is ever READ.
	 * Throws an exception if the access is not allowed.
	 * 
	 * @param dbManager
	 * @param table
	 * @param con
	 * @param ret
	 * @throws AccessDeniedException
	 */
	public abstract void hasReadAccess(DbManager dbManager, Table table, DbConnection con, DbResult ret) throws AccessDeniedException;

}
