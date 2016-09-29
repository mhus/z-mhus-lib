package de.mhus.lib.adb.model;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.IRelationObject;
import de.mhus.lib.annotations.adb.DbRelation;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.sql.DbConnection;

/**
 * <p>FieldRelation class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FieldRelation extends MObject {

	private DbManager manager;
	private DbRelation config;
	private Table table;
	private PojoAttribute<Object> attribute;

	/**
	 * <p>Constructor for FieldRelation.</p>
	 *
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 * @param table a {@link de.mhus.lib.adb.model.Table} object.
	 * @param attribute a {@link de.mhus.lib.core.pojo.PojoAttribute} object.
	 * @param config a {@link de.mhus.lib.annotations.adb.DbRelation} object.
	 */
	@SuppressWarnings("unchecked")
	public FieldRelation(DbManager manager, Table table, PojoAttribute<?> attribute, DbRelation config) {
		this.attribute = (PojoAttribute<Object>) attribute;
		this.manager = manager;
		this.config = config;
		this.table = table;
	}

	/**
	 * <p>getName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return attribute.getName();
	}

	/**
	 * <p>getRelationObject.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.adb.IRelationObject} object.
	 */
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

	/**
	 * <p>prepareCreate.</p>
	 *
	 * @param object a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 */
	public void prepareCreate(Object object) throws Exception {
		IRelationObject rel = getRelationObject(object);
		if (rel != null)
			rel.prepareCreate();
	}

	/**
	 * <p>created.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param object a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 */
	public void created(DbConnection con,Object object) throws Exception {
		IRelationObject rel = getRelationObject(object);
		if (rel != null)
			rel.created(con);
	}

	/**
	 * <p>loaded.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param object a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 */
	public void loaded(DbConnection con,Object object) throws Exception {
		IRelationObject rel = getRelationObject(object);
		if (rel != null)
			rel.loaded(con);
	}

	/**
	 * <p>saved.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param object a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 */
	public void saved(DbConnection con,Object object) throws Exception {
		IRelationObject rel = getRelationObject(object);
		if (rel != null)
			rel.saved(con);
	}

	/**
	 * <p>Getter for the field <code>table</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.adb.model.Table} object.
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * <p>Getter for the field <code>manager</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.adb.DbManager} object.
	 */
	public DbManager getManager() {
		return manager;
	}

	/**
	 * <p>Getter for the field <code>config</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.annotations.adb.DbRelation} object.
	 */
	public DbRelation getConfig() {
		return config;
	}

	/**
	 * <p>isChanged.</p>
	 *
	 * @param object a {@link java.lang.Object} object.
	 * @return a boolean.
	 */
	public boolean isChanged(Object object) {
		IRelationObject rel = getRelationObject(object);
		if (rel != null)
			return rel.isChanged();
		return false;
	}

	/**
	 * <p>prepareSave.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param object a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 */
	public void prepareSave(DbConnection con, Object object) throws Exception {
		IRelationObject rel = getRelationObject(object);
		if (rel != null)
			rel.prepareSave(con);
	}

	/**
	 * <p>inject.</p>
	 *
	 * @param object a {@link java.lang.Object} object.
	 */
	public void inject(Object object) {
		IRelationObject rel = getRelationObject(object);
		if (rel != null) rel.setManager(this, object); // again
	}

}
