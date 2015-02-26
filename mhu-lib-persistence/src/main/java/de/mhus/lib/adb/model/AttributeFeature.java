package de.mhus.lib.adb.model;

import de.mhus.lib.adb.DbManager;

public interface AttributeFeature {

	void init(DbManager manager, Field field);

	Object set(Object pojo, Object value);

	Object get(Object pojo, Object value);

}
