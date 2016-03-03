package de.mhus.lib.adb.model;

import de.mhus.lib.adb.DbAccessManager;
import de.mhus.lib.sql.DbConnection;

/**
 * <p>FeatureAccessManager class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FeatureAccessManager extends Feature {

	public DbAccessManager accessManager;

	/** {@inheritDoc} */
	@Override
	protected void doInit() {
		accessManager = manager.getSchema().getAccessManager(table);
	}
	
	/** {@inheritDoc} */
	@Override
	public void postFillObject(Object obj, DbConnection con) throws Exception {
		if (accessManager != null) accessManager.hasAccess(manager, table, con, obj, DbAccessManager.ACCESS.READ);
	}
	
	/** {@inheritDoc} */
	@Override
	public void preCreateObject(DbConnection con, Object object) throws Exception {
		if (accessManager != null) accessManager.hasAccess(manager, table, con, object, DbAccessManager.ACCESS.CREATE);
	}

	/** {@inheritDoc} */
	@Override
	public void preSaveObject(DbConnection con, Object object) throws Exception {
		if (accessManager != null) accessManager.hasAccess(manager, table, con, object, DbAccessManager.ACCESS.UPDATE);
	}

	/** {@inheritDoc} */
	@Override
	public void deleteObject(DbConnection con, Object object) throws Exception {
		if (accessManager != null) accessManager.hasAccess(manager, table, con, object, DbAccessManager.ACCESS.DELETE);
	}

	/** {@inheritDoc} */
	@Override
	public void postGetObject(DbConnection con, Object obj) throws Exception {
		if (accessManager != null) accessManager.hasAccess(manager, table, con, obj, DbAccessManager.ACCESS.READ);
	}

}
