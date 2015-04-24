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

	public void preCreateObject(DbConnection con, Object object) throws Exception {

	}

	public void preSaveObject(DbConnection con, Object object) throws Exception {

	}

	public void preGetObject(DbConnection con, DbResult ret) throws Exception {

	}

	public void postGetObject(DbConnection con, Object obj) throws Exception {

	}

	public void preFillObject(Object obj, DbConnection con, DbResult res) throws Exception {

	}

	public void deleteObject(DbConnection con, Object object) throws Exception {

	}

	public Object getValue(Object obj, Field field, Object val) throws Exception {
		return val;
	}

	public Object setValue(Object obj, Field field, Object value) throws Exception {
		return value;
	}

	public void postFillObject(Object obj, DbConnection con) throws Exception {
	}

	public void postCreateObject(DbConnection con, Object object) throws Exception {
	}

	public void postSaveObject(DbConnection con, Object object) throws Exception {
	}

	public void postFillObject(Object obj, DbConnection con, DbResult res) {
		// TODO Auto-generated method stub
		
	}

}
