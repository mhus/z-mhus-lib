package de.mhus.lib.adb;

import de.mhus.lib.adb.model.Table;
import de.mhus.lib.errors.AccessDeniedException;
import de.mhus.lib.sql.DbConnection;

/**
 * Skeleton to allow the validation of access rights. It's created in DbSchema.
 * 
 * @author mikehummel
 *
 */
public abstract class DbAccessManager {

	public static final String FEATURE_NAME = "accesscontrol";
	public enum ACCESS {READ, CREATE, UPDATE, DELETE};

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
	public abstract void hasAccess(DbManager manager, Table c, DbConnection con, Object object, ACCESS right) throws AccessDeniedException;

}
