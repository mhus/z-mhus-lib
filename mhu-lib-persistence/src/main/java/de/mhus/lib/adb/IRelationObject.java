package de.mhus.lib.adb;

import de.mhus.lib.adb.model.FieldRelation;
import de.mhus.lib.sql.DbConnection;

public interface IRelationObject {

	void prepareCreate() throws Exception;

	void created(DbConnection con) throws Exception;

	void saved(DbConnection con) throws Exception;

	void setManager(FieldRelation fieldRelation, Object obj);

	boolean isChanged();

	void loaded(DbConnection con);

	void prepareSave(DbConnection con) throws Exception;

}
