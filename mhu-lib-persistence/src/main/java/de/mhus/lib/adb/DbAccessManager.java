package de.mhus.lib.adb;

import de.mhus.lib.adb.model.Table;
import de.mhus.lib.errors.AccessDeniedException;
import de.mhus.lib.sql.DbConnection;

/**
 * Skeleton to allow the validation of access rights. It's created in DbSchema.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class DbAccessManager {

	/** Constant <code>FEATURE_NAME="accesscontrol"</code> */
	public static final String FEATURE_NAME = "accesscontrol";
	public enum ACCESS {READ, CREATE, UPDATE, DELETE};

	/**
	 * Throws an Exception if the access is not allowed.
	 *
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 * @param c
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param object a {@link java.lang.Object} object.
	 * @param right a {@link de.mhus.lib.adb.DbAccessManager.ACCESS} object.
	 * @throws de.mhus.lib.errors.AccessDeniedException if any.
	 */
	public abstract void hasAccess(DbManager manager, Table c, DbConnection con, Object object, ACCESS right) throws AccessDeniedException;

}
