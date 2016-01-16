package de.mhus.lib.adb;

import de.mhus.lib.errors.MException;
import de.mhus.lib.sql.DbConnection;

public interface DbObjectHandler {

	void saveObject(DbConnection con, String registryName, Object dbComfortableObject) throws MException;

	boolean objectChanged(Object dbComfortableObject) throws MException;

	void reloadObject(DbConnection con, String registryName, Object dbComfortableObject) throws MException;

	void deleteObject(DbConnection con, String registryName, Object dbComfortableObject) throws MException;

	void createObject(DbConnection con, Object dbComfortableObject) throws MException;

}
