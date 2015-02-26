package de.mhus.lib.adb;

import de.mhus.lib.sql.DbConnection;

/**
 * Interface to allow send hooks to the object.
 * 
 * @author mikehummel
 *
 */
public interface DbObject extends Persistable {

	void doPreCreate(DbConnection con);
	
	void doPreSave(DbConnection con);

	void doInit(DbManager manager, String registryName, boolean isPersistent);

	void doPreDelete(DbConnection con);

	void doPostLoad(DbConnection con);

	void doPostCreate(DbConnection con);
	
	void doPostDelete(DbConnection con);

	boolean isAdbPersistent();
	
}
