package de.mhus.lib.adb.model;

import de.mhus.lib.adb.DbAccessManager;
import de.mhus.lib.adb.DbManager;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DbResult;

public class FeatureAccessManager extends Feature {

	public DbAccessManager accessManager;

	protected void doInit() {
		accessManager = manager.getSchema().getAccessManager(table);
	}

	public void createObject(DbConnection con, Object object) throws Exception {
		if (accessManager != null) accessManager.hasAccess(manager, table, con, object, DbManager.R_CREATE);
	}
	
	public void saveObject(DbConnection con, Object object) throws Exception {
		if (accessManager != null) accessManager.hasAccess(manager, table, con, object, DbManager.R_UPDATE);
	}
	
	public void getObject(DbConnection con, DbResult ret) throws Exception {
		if (accessManager != null) accessManager.hasReadAccess(manager, table, con, ret);
	}

	public void fillObject(Object obj, DbConnection con, DbResult res) throws Exception {
		if (accessManager != null) accessManager.hasReadAccess(manager, table, con, res);
	}

	public void deleteObject(DbConnection con, Object object) throws Exception {
		if (accessManager != null) accessManager.hasAccess(manager, table, con, object, DbManager.R_DELETE);
	}
	
	public void checkFillObject(DbConnection con, DbResult res) throws Exception {
		if (accessManager != null) accessManager.hasReadAccess(manager, table, con, res);
	}

}
