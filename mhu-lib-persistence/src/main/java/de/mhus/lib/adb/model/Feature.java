package de.mhus.lib.adb.model;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.errors.MException;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DbResult;

public abstract class Feature extends MObject {

	protected DbManager manager;
	protected Table table;

	public void init(DbManager manager, Table table) throws MException {
		this.manager = manager;
		this.table = table;
		doInit();
	}

	protected void doInit() throws MException {
		
	}

	public void createObject(DbConnection con, Object object) throws Exception {
		
	}

	public void saveObject(DbConnection con, Object object) throws Exception {
		
	}

	public void getObject(DbConnection con, DbResult ret) throws Exception {
		
	}

	public void getObject(DbConnection con, Object obj) throws Exception {
		
	}

	public void fillObject(Object obj, DbConnection con, DbResult res) throws Exception {
		
	}

	public void removeObject(DbConnection con, Object object) throws Exception {
		
	}

	public Object get(Object obj, Field field, Object val) throws Exception {
		return val;
	}

	public Object set(Object obj, Field field, Object value) throws Exception {
		return value;
	}

	public void checkFillObject(DbConnection con, DbResult res) throws Exception {
	}

}
