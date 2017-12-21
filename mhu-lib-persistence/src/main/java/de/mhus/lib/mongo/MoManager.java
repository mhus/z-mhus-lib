package de.mhus.lib.mongo;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.FindAndModifyOptions;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.mongodb.MongoClient;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.Persistable;
import de.mhus.lib.adb.query.AAnd;
import de.mhus.lib.adb.query.AAttribute;
import de.mhus.lib.adb.query.ACompare;
import de.mhus.lib.adb.query.AConcat;
import de.mhus.lib.adb.query.ACreateContext;
import de.mhus.lib.adb.query.ADbAttribute;
import de.mhus.lib.adb.query.ADynValue;
import de.mhus.lib.adb.query.AEnumFix;
import de.mhus.lib.adb.query.AFix;
import de.mhus.lib.adb.query.ALimit;
import de.mhus.lib.adb.query.AList;
import de.mhus.lib.adb.query.ALiteral;
import de.mhus.lib.adb.query.ALiteralList;
import de.mhus.lib.adb.query.ANot;
import de.mhus.lib.adb.query.ANull;
import de.mhus.lib.adb.query.AOperation;
import de.mhus.lib.adb.query.AOr;
import de.mhus.lib.adb.query.AOrder;
import de.mhus.lib.adb.query.APart;
import de.mhus.lib.adb.query.APrint;
import de.mhus.lib.adb.query.AQuery;
import de.mhus.lib.adb.query.ASubQuery;
import de.mhus.lib.core.jmx.MJmx;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.errors.NotSupportedException;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.SqlDialectCreateContext;

public class MoManager extends MJmx implements MoHandler {

	private MongoClient client;
	private MoSchema schema;
	private Morphia morhia;
	private Datastore datastore;
	private LinkedList<Class<? extends Persistable>> managedTypes;

	public MoManager(MongoClient client, MoSchema schema) {
		this.client = client;
		this.schema = schema;
		initDatabase();
	}

	@SuppressWarnings("rawtypes")
	protected void initDatabase() {
		Mapper mapper = getMapper();
		Set<Class> classObjs = new HashSet<>();
		managedTypes = new LinkedList<>();
		schema.findObjectTypes(managedTypes);
		classObjs.addAll(managedTypes);
		morhia = new Morphia(mapper, classObjs );
		datastore = morhia.createDatastore(client, schema.getDatabaseName());
		datastore.ensureIndexes();
	}
	
	protected Mapper getMapper() {
        Mapper mapper = new Mapper();  
        schema.initMapper(mapper);
        return mapper;
    }
	
	public void save(Object obj) {
		datastore.save(obj);
	}
	
	public void delete(Object obj) {
		datastore.delete(obj);
	}
	
	public <T> T getObject(Class<T> clazz, Object ... keys) throws MException {
		return datastore.get(clazz, keys[0]);
	}
	
	public <T> Query<T> createQuery(Class<T> clazz) {
		return datastore.createQuery(clazz);
	}
		
	// ----
	
	@Override
	public void saveObject(DbConnection con, String registryName, Object dbComfortableObject) throws MException {
		save(dbComfortableObject);
	}

	@Override
	public boolean objectChanged(Object dbComfortableObject) throws MException {
		// TODO
		return true;
	}

	@Override
	public void reloadObject(DbConnection con, String registryName, Object dbComfortableObject) throws MException {
		//TODO
		throw new NotSupportedException();
	}

	@Override
	public void deleteObject(DbConnection con, String registryName, Object dbComfortableObject) throws MException {
		delete(dbComfortableObject);
	}

	@Override
	public void createObject(DbConnection con, Object dbComfortableObject) throws MException {
		save(dbComfortableObject);
	}

	// -----
	
	public <T> UpdateOperations<T> createUpdateOperations(Class<T> clazz) {
		return datastore.createUpdateOperations(clazz);
	}

	public <T> T findAndDelete(Query<T> query) {
		return datastore.findAndDelete(query);
	}

	public <T> T findAndModify(Query<T> query, UpdateOperations<T> operations, FindAndModifyOptions options) {
		return datastore.findAndModify(query, operations, options);
	}

	public <T> T findAndModify(Query<T> query, UpdateOperations<T> operations) {
		return datastore.findAndModify(query, operations);
	}

	public <T> long getCount(T entity) {
		return datastore.getCount(entity);
	}

	public <T> long getCount(Class<T> clazz) {
		return datastore.getCount(clazz);
	}

	public <T> long getCount(Query<T> query) {
		return datastore.getCount(query);
	}

	public <T> UpdateResults update(Query<T> query, UpdateOperations<T> operations) {
		return datastore.update(query, operations);
	}

	public void close() {
		
	}

	public MoSchema getSchema() {
		return schema;
	}

	public List<Class<? extends Persistable>> getManagedTypes() {
		return managedTypes;
	}

	public Class<?> getManagedType(String name) throws NotFoundException {
		name = name.trim().toLowerCase();
		for (Class<? extends Persistable> type : managedTypes)
			if (type.getCanonicalName().toLowerCase().endsWith(name)) return type;
		throw new NotFoundException("Type not found",name);
	}
	
}
