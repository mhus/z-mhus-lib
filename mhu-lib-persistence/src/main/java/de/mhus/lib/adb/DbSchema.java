package de.mhus.lib.adb;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.adb.model.AttributeFeature;
import de.mhus.lib.adb.model.AttributeFeatureCut;
import de.mhus.lib.adb.model.Feature;
import de.mhus.lib.adb.model.FeatureAccessManager;
import de.mhus.lib.adb.model.FeatureCut;
import de.mhus.lib.adb.model.Field;
import de.mhus.lib.adb.model.FieldPersistent;
import de.mhus.lib.adb.model.FieldVirtual;
import de.mhus.lib.adb.model.Table;
import de.mhus.lib.adb.model.TableAnnotations;
import de.mhus.lib.adb.model.TableDynamic;
import de.mhus.lib.adb.transaction.LockStrategy;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.annotations.adb.DbRelation;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.pojo.DefaultFilter;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoModelFactory;
import de.mhus.lib.core.pojo.PojoParser;
import de.mhus.lib.core.service.UniqueId;
import de.mhus.lib.errors.AccessDeniedException;
import de.mhus.lib.errors.MException;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DbPool;
import de.mhus.lib.sql.DbResult;

/**
 * Define the schema with a new instance of this class. It can handle and manipulate
 * all activities. It's also a factory for the loaded objects.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class DbSchema extends MObject implements PojoModelFactory {


	protected String tablePrefix = "";
	private LinkedList<Class<? extends Persistable>> objectTypes;
	protected LockStrategy lockStrategy; // set this object to enable locking

	/**
	 * <p>findObjectTypes.</p>
	 *
	 * @param list a {@link java.util.List} object.
	 */
	public abstract void findObjectTypes(List<Class<? extends Persistable>> list);
	
	/**
	 * <p>Getter for the field <code>objectTypes</code>.</p>
	 *
	 * @return an array of {@link java.lang.Class} objects.
	 */
	@SuppressWarnings("unchecked")
	public final Class<? extends Persistable>[] getObjectTypes() {
		initObjectTypes();
		return (Class<? extends Persistable>[]) objectTypes.toArray(new Class<?>[objectTypes.size()]);
	}

	void resetObjectTypes() {
		objectTypes = null;
	}
	
	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unchecked")
	public PojoModel createPojoModel(Class<?> clazz) {
		return new PojoParser().parse(clazz, "_", new Class[] { DbPersistent.class, DbPrimaryKey.class, DbRelation.class }).filter(true,false,true,false,true).getModel();
	}

	/**
	 * This should be called after the manager is created.
	 *
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 */
	public void doPostInit(DbManager manager) {
	}

	/**
	 * Overwrite this method to get the configuration object and initialize the schem. It should be
	 * called by the creator to initialize the schema before it is given to the manager.
	 *
	 * @param config a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 */
	public void doInit(ResourceNode config) {

	}

	/**
	 * Masquerade the table names if needed. By default a tablePrefix is set for the table.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getTableName(String name) {
		return tablePrefix + name;
	}


	/**
	 * Object factory to create different kinds of objects for one table.
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @param registryName a {@link java.lang.String} object.
	 * @param ret could be null, return the default object
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 * @param isPersistent a boolean.
	 * @throws java.lang.Exception if any.
	 * @return a {@link java.lang.Object} object.
	 */
	public Object createObject(Class<?> clazz, String registryName, DbResult ret, DbManager manager, boolean isPersistent) throws Exception {
		Object object = manager
				.getActivator()
				.createObject(clazz.getCanonicalName());
		if (object instanceof DbObject) {
			((DbObject)object).doInit(manager, registryName, isPersistent);
		}
		return object;
	}

	/**
	 * If no registryName is set in the manager this will ask the schema for the correct registryName.
	 *
	 * @param object a {@link java.lang.Object} object.
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 * @return a {@link java.lang.Class} object.
	 */
	public Class<? extends Persistable> findClassForObject(Object object, DbManager manager) {
		initObjectTypes();
		if (object instanceof Class<?>) {
			for (Class<? extends Persistable> c : objectTypes)
				if (((Class<?>)object).isAssignableFrom(c)) return c;
		}

		for (Class<? extends Persistable> c : objectTypes)
			if (c.isInstance(object)) return c;
		return null;
	}

	/**
	 * <p>initObjectTypes.</p>
	 */
	protected synchronized void initObjectTypes() {
		if (objectTypes != null) return;
		objectTypes = new LinkedList<>();
		findObjectTypes(objectTypes);
	}

	/**
	 * Return a new unique Id for a new entry in the table. Only used for auto_id fields with type long.
	 * The default implementation is not save !!!
	 *
	 * @param table a {@link de.mhus.lib.adb.model.Table} object.
	 * @param field a {@link de.mhus.lib.adb.model.Field} object.
	 * @param obj a {@link java.lang.Object} object.
	 * @param name a {@link java.lang.String} object.
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 * @since 3.2.9
	 */
	public void doCreateUniqueIdFor(Table table,Field field,Object obj, String name, DbManager manager) {
		
		// Ask Object-Class/Object to create an unique Id
		try {
			Method helperMethod = field.getType().getMethod("doCreateUniqueIdFor_" + field.getName(), new Class[] { DbManager.class});
			Object res = helperMethod.invoke(obj, new Object[] { manager });
			if (res == null) return;
			field.set(obj, res);
			return;
		} catch (NoSuchMethodException nsme) {
			log().t(field,nsme);
		} catch (Throwable t) {
			log().t(field,t);
			return;
		}
		
		
		
//		long id = base(UniqueId.class).nextUniqueId();
//		field.set(obj , id);
	}

	/**
	 * Overwrite this to get the hook in the schema. By default it's delegated to the object.
	 * Remember to call the super.
	 *
	 * @param table a {@link de.mhus.lib.adb.model.Table} object.
	 * @param object a {@link java.lang.Object} object.
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 */
	public void doPreCreate(Table table,Object object, DbConnection con, DbManager manager) {
		if (object instanceof DbObject) {
			((DbObject)object).doInit(manager, table.getRegistryName(), ((DbObject)object).isAdbPersistent() );
			((DbObject)object).doPreCreate(con);
		}
	}

	/**
	 * Overwrite this to get the hook in the schema. By default it's delegated to the object.
	 * Remember to call the super.
	 *
	 * @param table a {@link de.mhus.lib.adb.model.Table} object.
	 * @param object a {@link java.lang.Object} object.
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 */
	public void doPreSave(Table table,Object object, DbConnection con, DbManager manager) {
		if (object instanceof DbObject) {
			((DbObject)object).doPreSave(con);
		}
	}

	/**
	 * Return true if you want to store persistent information about the schema in the database.
	 * Use Manager.getSchemaProperties() to access the properties.
	 * Default value is true.
	 *
	 * @return a boolean.
	 */
	public boolean hasPersistentInfo() {
		return true;
	}

	/**
	 * Return the name of the schema used for example for the schema property table. Default
	 * is the simple name of the class.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSchemaName() {
		return getClass().getSimpleName();
	}

	/**
	 * Overwrite this if you want to provide default query attributes by default. Name mapping
	 * will provide all table and field names for the used db activities.
	 *
	 * @param nameMapping a {@link java.util.HashMap} object.
	 */
	public void doFillNameMapping(HashMap<String, Object> nameMapping) {

	}

	/**
	 * Overwrite this to get the hook in the schema. By default it's delegated to the object.
	 * Remember to call the super.
	 *
	 * @param table a {@link de.mhus.lib.adb.model.Table} object.
	 * @param object a {@link de.mhus.lib.adb.Persistable} object.
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param dbManager a {@link de.mhus.lib.adb.DbManager} object.
	 */
	public void doPreDelete(Table table, Persistable object, DbConnection con, DbManager dbManager) {
		if (object instanceof DbObject) {
			((DbObject)object).doPreDelete(con);
		}
	}

	/**
	 * Overwrite this to get the hook in the schema. By default it's delegated to the object.
	 * Remember to call the super.
	 *
	 * @param table a {@link de.mhus.lib.adb.model.Table} object.
	 * @param object a {@link de.mhus.lib.adb.Persistable} object.
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 */
	public void doPostLoad(Table table, Persistable object, DbConnection con, DbManager manager) {
		if (object instanceof DbObject) {
			((DbObject)object).doPostLoad(con);
		}
	}

	/**
	 * <p>doPostCreate.</p>
	 *
	 * @param table a {@link de.mhus.lib.adb.model.Table} object.
	 * @param object a {@link de.mhus.lib.adb.Persistable} object.
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 */
	public void doPostCreate(Table table, Persistable object, DbConnection con, DbManager manager) {
		if (object instanceof DbObject) {
			((DbObject)object).doPostCreate(con);
		}
	}
	/**
	 * Overwrite this to get the hook in the schema. By default it's delegated to the object.
	 * Remember to call the super.
	 *
	 * @param c
	 * @param object a {@link de.mhus.lib.adb.Persistable} object.
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param dbManager a {@link de.mhus.lib.adb.DbManager} object.
	 */
	public void doPostDelete(Table c, Persistable object, DbConnection con, DbManager dbManager) {
		if (object instanceof DbObject) {
			((DbObject)object).doPostDelete(con);
		}
	}

	/**
	 * Called if the schema property table is created. This allows the schema to add
	 * the default schema values to the properties set.
	 *
	 * @param dbManager a {@link de.mhus.lib.adb.DbManager} object.
	 */
	public void doInitProperties(DbManager dbManager) {

	}

	/**
	 * Overwrite this to validate the current database version and maybe migrate to a
	 * newer version.
	 * This only works if schema property is enabled.
	 * TODO Extend the default functionality to manage the versions.
	 *
	 * @param dbManager a {@link de.mhus.lib.adb.DbManager} object.
	 * @param currentVersion a long.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void doMigrate(DbManager dbManager, long currentVersion) throws MException {

	}

	/**
	 * If you provide access management return an access manager instance for the
	 * given table. This will most time be called one at initialization time.
	 *
	 * @param c a {@link de.mhus.lib.adb.model.Table} object.
	 * @return The manager or null
	 */
	public DbAccessManager getAccessManager(Table c) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		initObjectTypes();
		return MSystem.toString(this,getSchemaName(),objectTypes);
	}

	/**
	 * <p>createTable.</p>
	 *
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 * @param clazz a {@link java.lang.Class} object.
	 * @param registryName a {@link java.lang.String} object.
	 * @param tableName a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.adb.model.Table} object.
	 */
	public Table createTable(DbManager manager, Class<? extends Persistable> clazz,String registryName, String tableName) {

		boolean isDynamic = true;
		try {
			clazz.asSubclass(DbDynamic.class);
		} catch (ClassCastException e) {
			isDynamic = false;
		}

		Table table = null;
		if (isDynamic)
			table = new TableDynamic();
		else
			table = new TableAnnotations();
		table.init(manager,clazz,registryName,tableName);
		return table;
	}

	/**
	 * <p>createFeature.</p>
	 *
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 * @param table a {@link de.mhus.lib.adb.model.Table} object.
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.adb.model.Feature} object.
	 */
	public Feature createFeature(DbManager manager, Table table, String name) {

		try {
			Feature feature = null;

			name = name.trim().toLowerCase();

			if (name.equals("accesscontrol"))
				feature = new FeatureAccessManager();
			else
				if (name.equals(FeatureCut.NAME))
					feature = new FeatureCut();

			if (feature != null)
				feature.init(manager,table);
			else
				log().w("feature not found",name);
			return feature;
		} catch (Exception e) {
			log().t("feature",name,e);
			return null;
		}
	}

	/**
	 * <p>createAttributeFeature.</p>
	 *
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 * @param field a {@link de.mhus.lib.adb.model.Field} object.
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.adb.model.AttributeFeature} object.
	 */
	public AttributeFeature createAttributeFeature(DbManager manager,
			Field field, String name) {

		try {
			AttributeFeature feature = null;

			if (name.equals(AttributeFeatureCut.NAME)) {
				feature = new AttributeFeatureCut();
			}

			if (feature != null)
				feature.init(manager,field);
			else
				log().w("attribute feature not found",name);

			return feature;
		} catch (Exception e) {
			log().t("feature",name,e);
			return null;
		}
	}

	/**
	 * <p>createField.</p>
	 *
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 * @param table a {@link de.mhus.lib.adb.model.Table} object.
	 * @param pk a boolean.
	 * @param readOnly a boolean.
	 * @param virtual a boolean.
	 * @param attribute a {@link de.mhus.lib.core.pojo.PojoAttribute} object.
	 * @param attr a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 * @param dynamicField a {@link de.mhus.lib.adb.DbDynamic.Field} object.
	 * @param features an array of {@link java.lang.String} objects.
	 * @return a {@link de.mhus.lib.adb.model.Field} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public Field createField(DbManager manager, Table table, boolean pk, boolean readOnly, boolean virtual, PojoAttribute<?> attribute, ResourceNode attr,DbDynamic.Field dynamicField, String[] features) throws MException {

		Field field = null;
		if (virtual)
			field = new FieldVirtual( table, pk, attribute, attr, features );
		else
			field = new FieldPersistent( manager, table, pk, readOnly, attribute, attr, dynamicField, features );

		return field;
	}

	/**
	 * <p>internalCreateObject.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param name a {@link java.lang.String} object.
	 * @param object a {@link java.lang.Object} object.
	 * @param attributes a {@link java.util.HashMap} object.
	 */
	public void internalCreateObject(DbConnection con, String name, Object object,
			HashMap<String, Object> attributes) {
	}

	/**
	 * <p>internalSaveObject.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param name a {@link java.lang.String} object.
	 * @param object a {@link java.lang.Object} object.
	 * @param attributes a {@link java.util.HashMap} object.
	 */
	public void internalSaveObject(DbConnection con, String name, Object object,
			HashMap<String, Object> attributes) {
	}

	/**
	 * <p>internalDeleteObject.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param name a {@link java.lang.String} object.
	 * @param object a {@link java.lang.Object} object.
	 * @param attributes a {@link java.util.HashMap} object.
	 */
	public void internalDeleteObject(DbConnection con, String name, Object object,
			HashMap<String, Object> attributes) {
	}

	/**
	 * <p>onFillObjectException.</p>
	 *
	 * @param table a {@link de.mhus.lib.adb.model.Table} object.
	 * @param obj a {@link java.lang.Object} object.
	 * @param res a {@link de.mhus.lib.sql.DbResult} object.
	 * @param f a {@link de.mhus.lib.adb.model.Field} object.
	 * @param t a {@link java.lang.Throwable} object.
	 * @throws java.lang.Throwable if any.
	 */
	public void onFillObjectException(Table table, Object obj, DbResult res, Field f,
			Throwable t) throws Throwable {
		throw t;
	}

	/**
	 * Return a default connection if no connection is given for the operation with the object. If you want to
	 * work with transactions use this method to return a transaction bound connection. By default a new
	 * connection from the pool are used. You may overwrite the commit() or rollback() methods.
	 *
	 * @param pool a {@link de.mhus.lib.sql.DbPool} object.
	 * @throws java.lang.Exception if any.
	 * @return a {@link de.mhus.lib.sql.DbConnection} object.
	 * @since 3.2.9
	 */
	public DbConnection getConnection(DbPool pool) throws Exception {
		return pool.getConnection();
	}

	/**
	 * Close the default connection given with getConnection().
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @since 3.2.9
	 */
	public void closeConnection(DbConnection con) {
		con.close();
	}

	/**
	 * Used to commit a default connection. See getConnection()
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @throws java.lang.Exception if any.
	 * @since 3.2.9
	 */
	public void commitConnection(DbConnection con) throws Exception {
		con.commit();
	}

	/**
	 * <p>Getter for the field <code>lockStrategy</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.adb.transaction.LockStrategy} object.
	 * @since 3.2.9
	 */
	public LockStrategy getLockStrategy() {
		return lockStrategy;
	}

	/**
	 * <p>authorizeSaveForceAllowed.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param table a {@link de.mhus.lib.adb.model.Table} object.
	 * @param object a {@link java.lang.Object} object.
	 * @param raw a boolean.
	 * @throws de.mhus.lib.errors.AccessDeniedException if any.
	 * @since 3.2.9
	 */
	public void authorizeSaveForceAllowed(DbConnection con, Table table, Object object, boolean raw) throws AccessDeniedException {
		throw new AccessDeniedException();
	}

	/**
	 * <p>authorizeUpdateAttributes.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @param table a {@link de.mhus.lib.adb.model.Table} object.
	 * @param object a {@link java.lang.Object} object.
	 * @param raw a boolean.
	 * @param attributeNames a {@link java.lang.String} object.
	 * @throws de.mhus.lib.errors.AccessDeniedException if any.
	 */
	public void authorizeUpdateAttributes(DbConnection con, Table table,
			Object object, boolean raw, String ... attributeNames) throws AccessDeniedException {
		throw new AccessDeniedException();
	}

}
