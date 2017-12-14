package de.mhus.lib.adb;

import java.util.List;
import java.util.Map;

import de.mhus.lib.adb.model.Table;
import de.mhus.lib.adb.query.ACreateContext;
import de.mhus.lib.adb.query.AQuery;
import de.mhus.lib.adb.util.DbProperties;
import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.cao.util.MetadataBundle;
import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.jmx.MJmx;
import de.mhus.lib.errors.MException;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DbPool;
import de.mhus.lib.sql.DbResult;

/**
 * The implementation hold the table definitions and handle all operations on
 * the objects. It's the central point of access.
 */
@JmxManaged(descrition="ADB manager information interface")
public abstract class DbManager extends MJmx implements DbObjectHandler {

	public abstract <T> T getObjectByQualification(AQuery<T> qualification) throws MException;

	public abstract <T> DbCollection<T> getByQualification(Class<T> clazz, String qualification, Map<String,Object> attributes) throws MException;

	public abstract <T> DbCollection<T> getByQualification(T object, String qualification, Map<String,Object> attributes) throws MException;

	public abstract <T> DbCollection<T> getByQualification(AQuery<T> qualification) throws MException;

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
	public abstract <T> DbCollection<T> getByQualification(DbConnection con, T object, String registryName, String qualification, Map<String,Object> attributes) throws MException;

	public abstract String createSqlSelect(Class<?> clazz, String columns, String qualification);

	public abstract <T> long getCountAll(Class<T> clazz) throws MException;

	public abstract <T> long getCountByQualification(Class<T> clazz, String qualification, Map<String,Object> attributes) throws MException;

	public abstract <T> long getCountByQualification(T object, String qualification, Map<String,Object> attributes) throws MException;

	public abstract <T> long getCountByQualification(AQuery<T> qualification) throws MException;

	/**
	 * Returns the count of all found objects for the qualification. It's faster than
	 * loading all data from the database with getByQualification.
	 * 
	 * @param con
	 * @param object
	 * @param registryName
	 * @param qualification
	 * @param attributes
	 * @return x
	 * @throws MException
	 */
	public abstract <T> long getCountByQualification(DbConnection con, T object, String registryName, String qualification, Map<String,Object> attributes) throws MException;

	public abstract <T> long getMax(Class<T> clazz, String field) throws MException;

	public abstract <T> long getMaxByQualification(Class<T> clazz, String field, String qualification, Map<String,Object> attributes) throws MException;

	public abstract <T> long getMaxByQualification(T object, String field, String qualification, Map<String,Object> attributes) throws MException;

	public abstract <T> long getMaxByQualification(String field, AQuery<T> qualification) throws MException;

	public abstract <T> long getMaxByQualification(DbConnection con, T object, String registryName, String field, String qualification, Map<String,Object> attributes) throws MException;

	public abstract <T> DbCollection<T> executeQuery(T clazz, String query, Map<String,Object> attributes) throws MException;

	public abstract <T,R> List<R> getAttributeByQualification(Class<? extends T> clazz, String field, String qualification, Map<String,Object> attributes) throws MException;

	public abstract <T,R> List<R> getAttributedByQualification(String field, AQuery<? extends T> qualification) throws MException;

	public abstract <T,R> List<R> getAttributeByQualification(DbConnection con, Class<? extends T> clazz, String registryName, String field, String qualification, Map<String,Object> attributes) throws MException;

	/**
	 * Returns an collection.
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
	public abstract <T> DbCollection<T> executeQuery(DbConnection con, T clazz, String registryName, String query, Map<String,Object> attributes) throws MException;

	/**
	 * Returns a long value out of a query.
	 * 
	 * @param con
	 * @param attributeName
	 * @param query
	 * @param attributes
	 * @return x
	 * @throws MException
	 */
	public abstract <T> long executeCountQuery(DbConnection con, String attributeName, String query, Map<String,Object> attributes) throws MException;

	/**
	 * Returns the persistent schema properties if supported.
	 * @return The properties or null
	 */
	@JmxManaged(descrition="Database Properties of the Schema")
	public abstract DbProperties getSchemaProperties();

	public abstract Object getObject(String registryName, Object ... keys) throws MException;

	public abstract <T> T getObject(Class<T> clazz, Object ... keys) throws MException;

	public abstract <T> T getObject(DbConnection con, Class<T> clazz, Object ... keys) throws MException;

	/**
	 * Return an object from the database defined by the primary key - like a load operation.
	 * If more the one attributes is needed, the order is alphabetic by the attribute name.
	 * 
	 * @param con A connection to use or null
	 * @param registryName The registry name
	 * @param keys The primary key values
	 * @return x
	 * @throws MException
	 */
	public abstract Object getObject(DbConnection con, String registryName, Object ... keys) throws MException;

	//
	
	public abstract boolean existsObject(String registryName, Object ... keys) throws MException;

	abstract void fillObject(String registryName, Object object, DbConnection con, DbResult res) throws MException;

	public abstract <T> boolean existsObject(Class<T> clazz, Object ... keys) throws MException;

	public abstract <T> boolean existsObject(DbConnection con, Class<T> clazz, Object ... keys) throws MException;

	/**
	 * Return an object from the database defined by the primary key - like a load operation.
	 * If more the one attributes is needed, the order is alphabetic by the attribute name.
	 * 
	 * @param con A connection to use or null
	 * @param registryName The registry name
	 * @param keys The primary key values
	 * @return x
	 * @throws MException
	 */
	public abstract boolean existsObject(DbConnection con, String registryName, Object ... keys) throws MException;
	

	public abstract void reloadObject(String registryName, Object object) throws MException;

	/**
	 * Reload the object data from database.
	 * @param con The connection to use or null
	 * @param registryName The registry name or null
	 * @param object The object to fill
	 * @throws MException
	 */
	public abstract void reloadObject(DbConnection con, String registryName, Object object) throws MException;

	public abstract boolean objectChanged(Object object) throws MException;

	public abstract boolean objectChanged(String registryName, Object object) throws MException;

	/**
	 * Check the object date against the data in the database. If the data differs it will return true.
	 * 
	 * @param con
	 * @param registryName
	 * @param object
	 * @return x
	 * @throws MException
	 */
	public abstract boolean objectChanged(DbConnection con, String registryName, Object object) throws MException;

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
	public abstract void fillObject(DbConnection con, String registryName, Object object, Object ... keys) throws MException;

	public abstract void createObject(Object object) throws MException;

	public abstract void createObject(String registryName, Object object) throws MException;

	public abstract void createObject(DbConnection con, Object object) throws MException;

	/**
	 * Create a object in the database.
	 * 
	 * @param con The connection to use or null.
	 * @param registryName The registryName or null if it is a simple object reference
	 * @param object The object to create.
	 * @throws MException
	 */
	public abstract void createObject(DbConnection con, String registryName, Object object) throws MException;

	public abstract void saveObject(Object object) throws MException;

	public abstract void saveObject(String registryName, Object object) throws MException;

	public abstract void saveObject(DbConnection con, Object object) throws MException;

	/**
	 * Update the data of the object in the database. This will not create an object.
	 * 
	 * @param con The connection to use or null
	 * @param registryName The registryName or null
	 * @param object The object to save
	 * @throws MException
	 */
	public abstract void saveObject(DbConnection con, String registryName, Object object) throws MException;

	public abstract void saveObjectForce(Object object, boolean raw) throws MException;

	public abstract void saveObjectForce(String registryName, Object object, boolean raw) throws MException;

	public abstract void saveObjectForce(DbConnection con, Object object, boolean raw) throws MException;

	/**
	 * Update the data of the object in the database. This will not create an object.
	 * This update will also update read-only fields.
	 * 
	 * @param con The connection to use or null
	 * @param registryName The registryName or null
	 * @param object The object to save
	 * @param raw If true no events like preSave are called
	 * @throws MException
	 */
	public abstract void saveObjectForce(DbConnection con, String registryName, Object object, boolean raw) throws MException;
	
	public abstract void updateAttributes(Object object, boolean raw, String ... attributeNames) throws MException;

	public abstract void updateAttributes(String registryName, Object object, boolean raw, String ... attributeNames) throws MException;

	public abstract void updateAttributes(DbConnection con, Object object, boolean raw, String ... attributeNames) throws MException;

	public abstract void updateAttributes(DbConnection con, String registryName, Object object, boolean raw, String ... attributeNames) throws MException;
	
	public abstract void deleteObject(Object object) throws MException;

	public abstract void deleteObject(String registryName, Object object) throws MException;

	public abstract void deleteObject(DbConnection con, Object object) throws MException;

	/**
	 * Delete an object in the database.
	 * 
	 * @param con The connection to use or null
	 * @param registryName The registry name to use or null
	 * @param object The object to delete from database
	 * @throws MException
	 */
	public abstract void deleteObject(DbConnection con, String registryName, Object object) throws MException;
	
	public abstract boolean isConnected();

	public abstract void connect() throws Exception;

	public abstract void disconnect();

	public abstract void reconnect() throws Exception;


	@JmxManaged(descrition="Used Schema")
	public abstract DbSchema getSchema();

	public abstract DbPool getPool();

	public abstract MActivator getActivator();

	@JmxManaged(descrition="Current mapping of the table and column names")
	public abstract Map<String,Object> getNameMapping();

	public abstract MetadataBundle getCaoMetadata();
	
	@JmxManaged(descrition="Returns valide registry names")
	public abstract String[] getRegistryNames();

	@JmxManaged(descrition="Returns the table for the registry name")
	public abstract Table getTable(String registryName);

	public abstract Object createSchemaObject(String registryName) throws Exception;

	public abstract String getRegistryName(Object object);

	public abstract String getMappingName(Class<?> clazz);

	public abstract <T extends Persistable> T inject(T object);

	public abstract <T extends Persistable> DbCollection<T> getAll(Class<T> clazz) throws MException;

	public abstract <T> String toQualification(AQuery<T> qualification);
	
}
