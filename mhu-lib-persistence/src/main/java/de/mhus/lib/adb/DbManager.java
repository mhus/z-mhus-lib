package de.mhus.lib.adb;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import de.mhus.lib.adb.model.Field;
import de.mhus.lib.adb.model.Table;
import de.mhus.lib.adb.query.AQuery;
import de.mhus.lib.adb.util.DbProperties;
import de.mhus.lib.adb.util.Property;
import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.cao.util.MetadataBundle;
import de.mhus.lib.cao.util.NoneDriver;
import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.jmx.MJmx;
import de.mhus.lib.core.util.FallbackMap;
import de.mhus.lib.errors.MException;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DbPool;
import de.mhus.lib.sql.DbResult;
import de.mhus.lib.sql.DbStatement;

/**
 * The implementation hold the table definitions and handle all operations on
 * the objects. It's the central point of access.
 */
@JmxManaged(descrition="ADB manager information interface")
public class DbManager extends MJmx {
		
	public static final String DATABASE_VERSION = "db.version";
	public static final String DATABASE_CREATED = "db.created";
	public static final String DATABASE_MANAGER_VERSION = "db.manager.version";

	public static final String MANAGER_VERSION = "1.0";
	
	public static final int R_READ = 0;
	public static final int R_CREATE = 1;
	public static final int R_UPDATE = 2;
	public static final int R_REMOVE = 3;

	private DbSchema schema;
	private DbPool pool;
	private HashMap<String, Table> cIndex = new HashMap<String, Table>();
	private HashMap<String, Object> nameMapping;
	private Map<String, Object> nameMappingRO;
	
	private DbProperties schemaPersistence;
	private MetadataBundle caoBundle;
	private MActivator activator;

	public DbManager(DbPool pool, DbSchema schema) throws Exception {
		this(pool, schema, false);
	}

	/**
	 * Create a new manager instance, a db connection and a database schema is needed.
	 * 
	 * @param pool
	 * @param schema
	 * @throws Exception
	 */
	public DbManager(DbPool pool, DbSchema schema, boolean cleanup) throws Exception {
		this.pool   = pool;
		this.schema = schema;
		this.activator = pool.getProvider().getActivator();
		initDatabase(cleanup);
		
	}
	
	public DbManager(DbPool pool, DbSchema schema, MActivator activator) throws Exception {
		this(pool, schema, activator, false);
	}
	public DbManager(DbPool pool, DbSchema schema, MActivator activator, boolean cleanup) throws Exception {
		this.pool   = pool;
		this.schema = schema;
		this.activator = activator == null ? pool.getProvider().getActivator() : activator;
		initDatabase(cleanup);
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> DbCollection<T> getByQualification(Class<T> clazz, String qualification, Map<String,Object> attributes) throws MException {
		return (DbCollection<T>) getByQualification(null, (Object)clazz, null, qualification, attributes);
	}
	
	public <T> DbCollection<T> getByQualification(T object, String qualification, Map<String,Object> attributes) throws MException {
		return getByQualification(null, object, null, qualification, attributes);
	}

	@SuppressWarnings("unchecked")
	public <T> DbCollection<T> getByQualification(AQuery<T> qualification) throws MException {
		return (DbCollection<T>) getByQualification(null, qualification.getType(), null, qualification.toQualification(this), qualification.getAttributes(this));
	}
	
	/**
	 * Get an collection of objects by it's qualification. The qualification is the WHERE part of a
	 * query. e.g. "$db.table.name$ like 'Joe %'"
	 * 
	 * @param <T> Type of the object
	 * @param con The connection or null
	 * @param object a representation of the object (empty object) or a object to recycle in the collection.
	 * @param registryName The name of the registry (if not default) or null
	 * @param qualification The WHERE string
	 * @param attributes attributes or null if not needed
	 * @return A collection with the results
	 * @throws MException
	 */
	public <T> DbCollection<T> getByQualification(DbConnection con, T object, String registryName, String qualification, Map<String,Object> attributes) throws MException {
		
		Class<?> clazz = schema.findClassForObject(object,this);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM $db.").append(getMappingName(clazz)).append("$ ");
		if (MString.isSet(qualification)) {
			String low = qualification.trim().toLowerCase();
			if (low.startsWith("order"))
				sql.append(qualification);
			else
				sql.append("WHERE ").append(qualification);
		}
		
		return executeQuery(con, object, registryName, sql.toString(), attributes);
	}
	
	public <T> DbCollection<T> executeQuery(T clazz, String query, Map<String,Object> attributes) throws MException {
		
		return executeQuery(null, clazz, null, query, attributes);
	}
	
	/**
	 * Return an collection.
	 * 
	 * @param <T>
	 * @param con DbConnection or null
	 * @param clazz Empty Object class
	 * @param registryName registry name or null
	 * @param query The query, remember to return all attributes
	 * @param attributes attributes for the query or null
	 * @return a collection with the results
	 * @throws MException
	 */
	public <T> DbCollection<T> executeQuery(DbConnection con, T clazz, String registryName, String query, Map<String,Object> attributes) throws MException {
		Map<String, Object> map = null;
		
		DbConnection myCon = null;
		if (con == null) {
			try {
				myCon = pool.getConnection();
			} catch (Throwable t) {
				throw new MException(con,query,attributes,t);
			}
			con = myCon;
		}
		if (attributes == null)
			map = nameMappingRO;
		else
			map = new FallbackMap<String, Object>(attributes, nameMappingRO, true);
		try {
			DbStatement sth = con.createStatement(query);
			DbResult res = sth.executeQuery(map);
			return new DbCollection<T>(this,con,myCon != null,registryName,clazz,res);
		} catch (Throwable t) {
			throw new MException(con,query,attributes,t);
		} finally {
		// do not close, it's used by the collection
		//	if (myCon != null)
		//		myCon.close();
		}
	}
	
	/**
	 * Returns the persistent schema properties if supported.
	 * @return The properties or null
	 */
	@JmxManaged(descrition="Database Properties of the Schema")
	public DbProperties getSchemaProperties() {
		return schemaPersistence;
	}
	
	public Object getObject(String registryName, Object ... keys) throws MException {
		return getObject(null,registryName, keys);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getObject(Class<T> clazz, Object ... keys) throws MException {
		return (T)getObject(null,getRegistryName(clazz), keys);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getObject(DbConnection con, Class<T> clazz, Object ... keys) throws MException {
		return (T)getObject(con,getRegistryName(clazz), keys);
	}
	
	/**
	 * Return an object from the database defined by the primary key - like a load operation.
	 * If more the one attributes is needed, the order is alphabetic by the attribute name.
	 * 
	 * @param con A connection to use or null
	 * @param registryName The registry name
	 * @param keys The primary key values
	 * @return
	 * @throws MException
	 */
	public Object getObject(DbConnection con, String registryName, Object ... keys) throws MException {
		
//		registryName = registryName.toLowerCase();
		
		DbConnection myCon = null;
		if (con == null) {
			try {
				myCon = pool.getConnection();
				con = myCon;
			} catch (Throwable t) {
				throw new MException(t);
			}
		}
		
		Table c = cIndex.get(registryName);
		if (c == null)
			throw new MException("class definition not found in schema",registryName);
		
		try {
			Object out = c.getObject(con,keys);
			
			if (myCon != null) {
				try {
					myCon.commit();
				} catch (Throwable t) {
					throw new MException(t);
				}
				myCon.close();
			}
			
			schema.doPostLoad(c, out, con, this);

			return out;
		} catch (Throwable t) {
			throw new MException(registryName,t);
		}
	}

	public void checkFillObject(String registryName, Object object, DbConnection con, DbResult res) throws MException {

		if (registryName == null) {
			Class<?> clazz = getSchema().findClassForObject(object,this);
			if (clazz == null)
				throw new MException("class definition not found for object",object.getClass().getCanonicalName(),registryName);
			registryName = getRegistryName(clazz);
		}

		Table c = cIndex.get(registryName);
		if (c == null)
			throw new MException("class definition not found in schema",registryName);
		
		try {
			c.checkFillObject(con,res);
			
//			schema.doPostLoad(c,object,con, this);
			
		} catch (Throwable t) {
			throw new MException(registryName,t);
		}

	}
	
	/**
	 * Fill an object with values.
	 * @param registryName The registry name or null
	 * @param object The object to fill
	 * @param con The connection - not null
	 * @param res The databse resultset with the data
	 * @throws MException
	 */
	void fillObject(String registryName, Object object, DbConnection con, DbResult res) throws MException {

		if (registryName == null) {
			Class<?> clazz = getSchema().findClassForObject(object,this);
			if (clazz == null)
				throw new MException("class definition not found for object",object.getClass().getCanonicalName(),registryName);
			registryName = getRegistryName(clazz);
		}

		Table c = cIndex.get(registryName);
		if (c == null)
			throw new MException("class definition not found in schema",registryName);
		
		try {
			if (object == null) {
				
				object = schema.createObject(c.getClazz(), registryName, res, this, true);
			}
			c.fillObject(object,con,res);
			
			schema.doPostLoad(c,object,con, this);
			
		} catch (Throwable t) {
			throw new MException(registryName,t);
		}
	}
	
	public void reloadObject(String registryName, Object object) throws MException {
		reloadObject(null,registryName, object);
	}
	
	/**
	 * Reload the object data from database.
	 * @param con The connection to use or null
	 * @param registryName The registry name or null
	 * @param object The object to fill
	 * @throws MException
	 */
	public void reloadObject(DbConnection con, String registryName, Object object) throws MException {

		DbConnection myCon = null;
		if (con == null) {
			try {
				myCon = pool.getConnection();
				con = myCon;
			} catch (Throwable t) {
				throw new MException(t);
			}
		}
		
		if (registryName == null) {
			Class<?> clazz = schema.findClassForObject(object,this);
			if (clazz == null)
				throw new MException("class definition not found for object",object.getClass().getCanonicalName(),registryName);
			registryName = getRegistryName(clazz);
		}

		Table c = cIndex.get(registryName);
		if (c == null)
			throw new MException("class definition not found in schema",registryName);

		LinkedList<Object> keys = new LinkedList<Object>();
		try {
			for (Field f : c.getPrimaryKeys()) {
				keys.add(f.getFromTarget(object));
			}
			
			if (c.fillObject(con,object,keys.toArray()) == null)
				throw new MException("object not found");
			
			schema.doPostLoad(c,object,con,this);
			
		} catch (Throwable t) {
			throw new MException(registryName,t);
		}
		
		if (myCon != null) {
			try {
				myCon.commit();
			} catch (Throwable t) {
				throw new MException(t);
			}
			myCon.close();
		}
	}

	public boolean objectChanged(Object object) throws MException {
		return objectChanged(null,null, object);
	}

	public boolean objectChanged(String registryName, Object object) throws MException {
		return objectChanged(null,registryName, object);
	}

	/**
	 * Check the object date against the data in the database. If the data differs it will return true.
	 * 
	 * @param con
	 * @param registryName
	 * @param object
	 * @return
	 * @throws MException
	 */
	public boolean objectChanged(DbConnection con, String registryName, Object object) throws MException {

		DbConnection myCon = null;
		if (con == null) {
			try {
				myCon = pool.getConnection();
				con = myCon;
			} catch (Throwable t) {
				throw new MException(t);
			}
		}
		
		if (registryName == null) {
			Class<?> clazz = schema.findClassForObject(object,this);
			if (clazz == null)
				throw new MException("class definition not found for object",object.getClass().getCanonicalName(),registryName);
			registryName = getRegistryName(clazz);
		}

		Table c = cIndex.get(registryName);
		if (c == null)
			throw new MException("class definition not found in schema",registryName);

		boolean ret = false;
		LinkedList<Object> keys = new LinkedList<Object>();
		try {
			for (Field f : c.getPrimaryKeys()) {
				keys.add(f.getFromTarget(object));
			}
			
			ret = c.objectChanged(con,object,keys.toArray());
			
			// schema.doPostLoad(c,object,con,this);
			
		} catch (Throwable t) {
			throw new MException(registryName,t);
		}
		
		if (myCon != null) {
			try {
				myCon.commit();
			} catch (Throwable t) {
				throw new MException(t);
			}
			myCon.close();
		}
		
		return ret;
	}
	
	/**
	 * Fill a object with the values of an entry. This can cause big problems and exceptions if the
	 * kind of the object is not correct. This is used to recycle objects. It's not save in every case.
	 * 
	 * @param con The connection to use or null
	 * @param registryName The rgistryName or null
	 * @param object The object to fill
	 * @param keys The primary keys
	 * @throws MException
	 */
	public void fillObject(DbConnection con, String registryName, Object object, Object ... keys) throws MException {

		DbConnection myCon = null;
		if (con == null) {
			try {
				myCon = pool.getConnection();
				con = myCon;
			} catch (Throwable t) {
				throw new MException(t);
			}
		}
		
		if (registryName == null) {
			Class<?> clazz = schema.findClassForObject(object,this);
			if (clazz == null)
				throw new MException("class definition not found for object",object.getClass().getCanonicalName(),registryName);
			registryName = getRegistryName(clazz);
		}

		Table c = cIndex.get(registryName);
		if (c == null)
			throw new MException("class definition not found in schema",registryName);
		
		try {
			c.fillObject(con,object,keys);
			
			schema.doPostLoad(c,object,con,this);
			
		} catch (Throwable t) {
			throw new MException(registryName,t);
		}
		
		if (myCon != null) {
			try {
				myCon.commit();
			} catch (Throwable t) {
				throw new MException(t);
			}
			myCon.close();
		}
		
	}
		
	public void createObject(Object object) throws MException {
		createObject(null,null,object);
	}
	
	public void createObject(String registryName, Object object) throws MException {
		createObject(null, registryName, object);
	}
	
	public void createObject(DbConnection con, Object object) throws MException {
		createObject(con, null, object);
	}
	
	/**
	 * Create a object in the database.
	 * 
	 * @param con The connection to use or null.
	 * @param registryName The registryName or null if it is a simple object reference
	 * @param object The object to create.
	 * @throws MException
	 */
	public void createObject(DbConnection con, String registryName, Object object) throws MException {
		
		DbConnection myCon = null;
		if (con == null) {
			try {
				myCon = pool.getConnection();
				con = myCon;
			} catch (Throwable t) {
				throw new MException(t);
			}
		}
		
		if (registryName == null) {
			Class<?> clazz = schema.findClassForObject(object,this);
			if (clazz == null)
				throw new MException("class definition not found for object",object.getClass().getCanonicalName(),registryName);
			registryName = getRegistryName(clazz);
		}
		Table c = cIndex.get(registryName);
		if (c == null)
			throw new MException("class definition not found in schema",registryName);
		
		try {
			// prepare object
			c.prepareCreate(object);
			schema.doPreCreate(c,object,con,this);
			
			//save object
		
			c.createObject(con,object);
			
			schema.doPostLoad(c,object,con,this);

		} catch (Throwable t) {
			throw new MException(registryName,t);
		}
		
		if (myCon != null) {
			try {
				myCon.commit();
			} catch (Throwable t) {
				throw new MException(t);
			}
			myCon.close();
		}
	}
	
	public void saveObject(Object object) throws MException {
		saveObject(null, null, object);
	}
	
	public void saveObject(String registryName, Object object) throws MException {
		saveObject(null,registryName,object);
	}
	
	public void saveObject(DbConnection con, Object object) throws MException {
		saveObject(con, null, object);
	}
	
	/**
	 * Update the data of the object in the database. This will not create an object.
	 * 
	 * @param con The connection to use or null
	 * @param registryName The registryName or null
	 * @param object The object to save
	 * @throws MException
	 */
	public void saveObject(DbConnection con, String registryName, Object object) throws MException {
		
		DbConnection myCon = null;
		if (con == null) {
			try {
				myCon = pool.getConnection();
				con = myCon;
			} catch (Throwable t) {
				throw new MException(t);
			}
		}
		
		if (registryName==null) {
			Class<?> clazz = schema.findClassForObject(object,this);
			if (clazz == null)
				throw new MException("class definition not found for object",object.getClass().getCanonicalName());
			registryName = getRegistryName(clazz);
		}
		Table c = cIndex.get(registryName);
		if (c == null)
			throw new MException("class definition not found in schema",registryName);
		
		try {
			// prepare object
			schema.doPreSave(c,object,con,this);
			
			//save object
			c.saveObject(con,object);
		} catch (Throwable t) {
			throw new MException(registryName,t);
		}
		
		if (myCon != null) {
			try {
				myCon.commit();
			} catch (Throwable t) {
				throw new MException(t);
			}
			myCon.close();
		}
	}
	
	public void removeObject(Object object) throws MException {
		removeObject(null, null, object);
	}
	
	public void removeObject(String registryName, Object object) throws MException {
		removeObject(null, registryName, object);
	}
	
	public void removeObject(DbConnection con, Object object) throws MException {
		removeObject(con, null, object);
	}
	
	/**
	 * Remove an object in the database.
	 * 
	 * @param con The connection to use or null
	 * @param registryName The registry name to use or null
	 * @param object The object to remove from database
	 * @throws MException
	 */
	public void removeObject(DbConnection con, String registryName, Object object) throws MException {

		DbConnection myCon = null;
		if (con == null) {
			try {
				myCon = pool.getConnection();
				con = myCon;
			} catch (Throwable t) {
				throw new MException(t);
			}
		}
		
		if (registryName == null) {
			Class<?> clazz = schema.findClassForObject(object,this);
			if (clazz == null)
				throw new MException("class definition not found for object",object.getClass().getCanonicalName());
			registryName = getRegistryName(clazz);
		}
		Table c = cIndex.get(registryName);
		if (c == null)
			throw new MException("class definition not found in schema",registryName);
		
		try {
			// prepare object
			schema.doPreRemove(c,object,con,this);
			
			//save object
			c.removeObject(con,object);
			
			schema.doPostRemove(c,object,con,this);

		} catch (Throwable t) {
			throw new MException(registryName,t);
		}
		
		if (myCon != null) {
			try {
				myCon.commit();
			} catch (Throwable t) {
				throw new MException(t);
			}
			myCon.close();
		}
		
	}
	
	public boolean isConnected() {
		return nameMapping != null;
	}
	
	public void connect() throws Exception {
		initDatabase(false);
	}
	
	/**
	 * Initialize the database schema.
	 * 
	 * @throws Exception
	 */
	protected void initDatabase(boolean cleanup) throws Exception {
		
		if (nameMapping != null) return;
		
		Class<?>[] types = schema.getObjectTypes();
		DbConnection con = pool.getConnection();
		if (con == null) return;
		
		cIndex.clear();
		nameMapping = new HashMap<String, Object>();
		nameMappingRO = Collections.unmodifiableMap(nameMapping);
    	caoBundle = new MetadataBundle(NoneDriver.getInstance());

		// schema info
		if (schema.hasPersistentInfo()) {
			addClass(schema.getSchemaName(),getRegistryName(schema.getClass()),Property.class,con, cleanup);
			schemaPersistence = new DbProperties(this, getRegistryName(schema.getClass()));
		}
		
		// classes
		for (Class<?> clazz : types) {
			addClass(null,getRegistryName(clazz),clazz,con, cleanup);
		}
		con.commit();
		
		// fill name mapping
		for (Table c : cIndex.values()) {
			c.fillNameMapping(nameMapping);
		}
		
		schema.doFillNameMapping(nameMapping);
		
		// validate and migrate database version
		if (schemaPersistence != null) {
			String dbVersion = schemaPersistence.get(DATABASE_VERSION);
			if (dbVersion == null) {
				// init persistence
				schemaPersistence.set(DATABASE_VERSION,"0");
				schemaPersistence.set(DATABASE_CREATED,new MDate().toString());
				schemaPersistence.set(DATABASE_MANAGER_VERSION,MANAGER_VERSION);
				schema.doInitProperties(this);
				
				dbVersion = schemaPersistence.get(DATABASE_VERSION);
			}
			
			// migrate to current version
			long dbVersionLong = MCast.tolong(dbVersion, 0);
			schema.doMigrate(this, dbVersionLong);
			
		}
		
	}
	
	protected void addClass(String tableName, String registryName, Class<?> clazz, DbConnection con, boolean cleanup) throws Exception {
		Table c = schema.createTable(this,clazz,registryName,tableName);
		c.initDatabase(con, cleanup);
//		c.registryName = registryName;
//		parseClass(c,tableName);
//		c.createTable(con);
//		c.postInit();
		cIndex.put(registryName,c);
	}
	
	
	
//	protected void parseClass(Table c, String tableName) throws Exception {
//		Class<?> clazz = c.clazz;
//		DbTable table = clazz.getAnnotation(DbTable.class);
//		if (tableName != null) {
//			c.name = tableName;
//		} else
//		if (table == null) {
//			c.name = clazz.getSimpleName();
//		} else {
//			c.name = table.value();
//		}
//		c.tableNameOrg = schema.getTableName(c.name);
//		c.tableName = pool.getDialect().normalizeTableName(c.tableNameOrg);
//		
//		log().t("new table",c.name,c.tableName);
//		
//		c.isDynamic = true;
//		try {
//			clazz.asSubclass(DbDynamic.class);
//		} catch (ClassCastException e) {
//			c.isDynamic = false;
//		}
//
//		// parse fields
//		if (c.isDynamic) {
//			DbDynamic.Field[] fa = ((DbDynamic)clazz.newInstance()).getFieldDefinitions();
//			for (DbDynamic.Field f : fa) {
//				Field field =f.isPersistent() ? new PersistentField( f ) : new VirtualField( f );
//				c.addField( field );
//				
//				// indexes
//				String indexes = f.getIndexes();
//				if (indexes != null) {
//					c.addToIndex(indexes,field);
//				}
//
//			}
//		} else {
//			for (Method m : clazz.getMethods()) {
//				DbPrimaryKey pk = m.getAnnotation(DbPrimaryKey.class);
//				DbAttributes pa = m.getAnnotation(DbAttributes.class);
//				DbPersistent p  = m.getAnnotation(DbPersistent.class);
//				DbVirtual    v  = m.getAnnotation(DbVirtual.class);
//				DbIndex idx = m.getAnnotation(DbIndex.class);
//	
//	//			for ( Annotation a : m.getAnnotations()) {
//	//				System.out.println(m.getName() + ": " + a.toString());
//	//			}
//				
//				if (pk != null || p != null || pa != null || v != null) {
//					
//					if (p == null) p = new DbPersistent() {public Class<? extends Annotation> annotationType() {return null;}};
//	
//					String mName = m.getName();
//					Method getter = null;
//					Method setter = null;
//					if (mName.startsWith("get")) {
//						mName = mName.substring(3);
//						getter = m;
//						setter = clazz.getMethod("set" + mName,getter.getReturnType());
//					} else
//					if (mName.startsWith("set")) {
//						mName = mName.substring(3);
//						setter = m;
//						try {
//							getter = clazz.getMethod("get" + mName);
//						} catch (NoSuchMethodException nsme) {
//							getter = clazz.getMethod("is" + mName);
//						}
//					} else
//					if (mName.startsWith("is")) {
//						mName = mName.substring(2);
//						getter = m;
//						setter = clazz.getMethod("set" + mName,getter.getReturnType());
//					} else {
//						log().d("field is not a getter/setter" + mName);
//						continue;
//					}
//					
//					if (getter == null) {
//						log().d("getter not found",mName);
//						continue;
//					}
//					if (setter == null) {
//						log().d("setter not found",mName);
//						continue;
//					}
//					Class<?> ret = getter.getReturnType();
//					if (ret == void.class) {
//						log().d("Value type is void - ignore");
//						continue;
//					}
//					
//					
//					if (c.fIndex.containsKey(mName.toLowerCase())) {
//						log().d("field already defined", mName);
//						continue;
//					}
//					
//					Map<String, String> attr = Rfc1738.explode(toAttributes(pa));
//					log().t("field",mName);
//					Field field = null;
//					if (v != null)
//						field = new FieldVirtual( mName, pk!=null, setter, getter, ret, attr );
//					else
//						field = new PersistentField( mName, pk!=null, setter, getter, ret, attr );
//					c.addField( field );
//					
//					// indexes
//					if (idx != null && field.isPersistent()) {
//						c.addToIndex(idx.value(),field);
//					}
//						
//				}
//			}
//		}
//		
//		
//		c.accessManager = schema.getAccessManager(c);
////		if (c.accessManager != null) {
////			c.accessManager.doInit(this);
////		}
//	}

	/**
	 * Representation of one Table in the registry.
	 * 
	 * @author mikehummel
	 *
	 */
//	public class Table {
//
//		public boolean isDynamic;
//		public DbAccessManager accessManager;
//		private String registryName;
//		private String name;
//		private String tableNameOrg;
//		private String tableName;
//		private Class<?> clazz;
//		private HashMap<String,Field> fIndex = new HashMap<String, DbManager.Field>();
//		private LinkedList<Field> fList = new LinkedList<DbManager.Field>();
//		private LinkedList<Field> pk = new LinkedList<DbManager.Field>();
//		private DbPrepared sqlPrimary;
//		private DbPrepared sqlInsert;
//		private DbPrepared sqlUpdate;
//		private HashMap<String, LinkedList<Field>> iIdx = new HashMap<String, LinkedList<Field>>();
//		private DbPrepared sqlRemove;
//
//		public Table(Class<?> clazz) {
//			this.clazz = clazz;
//		}
//
//		public void removeObject(DbConnection con, Object object) throws Exception {
//			
//			if (accessManager != null) accessManager.hasAccess(DbManager.this, this, con, object, R_REMOVE);
//
//			HashMap<String, Object> attributes = new HashMap<String, Object>();
//			for (Field f : pk) {
//				attributes.put(f.name, f.getFromTarget(object));
//			}
//			sqlRemove.getStatement(con).execute(attributes);
//		}
//
//		private void addField(Field field) {
//			field.table = this;
//			fIndex.put(field.createName, field);
//			fList.add(field);
//			if (field.isPrimary && field.isPersistent()) pk.add(field);
//		}
//		
//		private void fillNameMapping() throws Exception {
//			nameMapping.put("db." + name, new Raw(tableName));
//			for (Field f : fList) {
//				f.fillNameMapping();
//			}
//		}
//
//		private void addToIndex(String list, Field field) {
//			for (String nr : MString.split(list, ",")) {
//				LinkedList<Field> list2 = iIdx.get(nr);
//				if (list2 == null) {
//					list2 = new LinkedList<Field>();
//					iIdx.put(nr, list2);
//				}
//				list2.add(field);
//			}
//		}
//		
//	
//	}
//	
	@JmxManaged(descrition="Used Schema")
	public DbSchema getSchema() {
		return schema;
	}

	public DbPool getPool() {
		return pool;
	}
	
	public MActivator getActivator() {
		return activator;
	}
	
	@JmxManaged(descrition="Current mapping of the table and column names")
	public Map<String,Object> getNameMapping() {
		return nameMappingRO;
	}
	
	public MetadataBundle getCaoMetadata() {
		return caoBundle;
	}

	@JmxManaged(descrition="Returns valide registry names")
	public String[] getRegistryNames() {
		return cIndex.keySet().toArray(new String[0]);
	}
	
	@JmxManaged(descrition="Returns the table for the registry name")
	public Table getTable(String registryName) {
		return cIndex.get(registryName);
	}
	
	public Object createSchemaObject(String registryName) throws Exception {
		Table table = cIndex.get(registryName);
		if (table == null) throw new MException("class definition not found in schema",registryName);
		return schema.createObject(table.getClazz(), table.getRegistryName(), null, this, false);
	}
	
	public String getRegistryName(Object object) {
		if (object instanceof Class<?>) {
			return ((Class<?>)object).getCanonicalName();
		}
		Class<?> clazz = schema.findClassForObject(object,this);
		if (clazz == null) return null;
		return clazz.getCanonicalName();
	}

	public String getMappingName(Class<?> clazz) {
			return clazz.getSimpleName().toLowerCase();
	}
	
	public <T> T injectObject(T object) {
		getTable(getRegistryName(object)).injectObject(object);
		return object;
	}

}
