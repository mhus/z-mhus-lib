package de.mhus.lib.adb.model;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.IRelationObject;
import de.mhus.lib.annotations.adb.DbRelation;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.sql.DbConnection;

public class FieldRelation extends MObject {

	private DbManager manager;
	private DbRelation config;
	private Table table;
	private PojoAttribute<Object> attribute;

	@SuppressWarnings("unchecked")
	public FieldRelation(DbManager manager, Table table, PojoAttribute<?> attribute, DbRelation config) {
		this.attribute = (PojoAttribute<Object>) attribute;
		this.manager = manager;
		this.config = config;
		this.table = table;
	}

	public String getName() {
		return attribute.getName();
	}

	public IRelationObject getRelationObject(Object obj) {
		try {
			IRelationObject rel = (IRelationObject)attribute.get(obj);
			if (rel == null && attribute.canWrite()) {
				rel = (IRelationObject) attribute.getType().newInstance();
				attribute.set(obj, rel);
			}
			if (rel != null)
				rel.setManager(this, obj);
			return rel;
		} catch (Throwable e) {
			log().t(getName(),obj,e);
		}
		return null;
	}

	public void prepareCreate(Object object) throws Exception {
		IRelationObject rel = getRelationObject(object);
		if (rel != null)
			rel.prepareCreate();
	}

	public void created(DbConnection con,Object object) throws Exception {
		IRelationObject rel = getRelationObject(object);
		if (rel != null)
			rel.created(con);
	}

	public void loaded(DbConnection con,Object object) throws Exception {
		IRelationObject rel = getRelationObject(object);
		if (rel != null)
			rel.loaded(con);
	}

	public void saved(DbConnection con,Object object) throws Exception {
		IRelationObject rel = getRelationObject(object);
		if (rel != null)
			rel.saved(con);
	}

	public Table getTable() {
		return table;
	}

	public DbManager getManager() {
		return manager;
	}

	public DbRelation getConfig() {
		return config;
	}

	public boolean isChanged(Object object) {
		IRelationObject rel = getRelationObject(object);
		if (rel != null)
			return rel.isChanged();
		return false;
	}

	public void prepareSave(DbConnection con, Object object) throws Exception {
		IRelationObject rel = getRelationObject(object);
		if (rel != null)
			rel.prepareSave(con);
	}

	public void inject(Object object) {
		IRelationObject rel = getRelationObject(object);
		if (rel != null) rel.setManager(this, object); // again
	}

}
