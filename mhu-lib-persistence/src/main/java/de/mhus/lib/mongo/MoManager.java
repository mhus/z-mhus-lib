package de.mhus.lib.mongo;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;

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
import de.mhus.lib.errors.NotSupportedException;
import de.mhus.lib.sql.DbConnection;

public class MoManager extends MJmx implements MoHandler {

	private MongoClient client;
	private MoSchema schema;
	private Morphia morhia;
	private Datastore datastore;

	public MoManager(MongoClient client, MoSchema schema) {
		this.client = client;
		this.schema = schema;
		initDatabase();
	}

	@SuppressWarnings("rawtypes")
	protected void initDatabase() {
		Mapper mapper = getMapper();
		Set<Class> classObjs = new HashSet<>();
		LinkedList<Class<? extends Persistable>> list = new LinkedList<>();
		schema.findObjectTypes(list);
		classObjs.addAll(list);
		morhia = new Morphia(mapper, classObjs );
		datastore = morhia.createDatastore(client, schema.getDatabaseName());
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
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getByQualification(AQuery<T> qualification) throws MException {
		Query<? extends T> q = datastore.createQuery(qualification.getType());
		doQualification(qualification, q);
		return (List<T>) q.asList();
	}

	private <T> void doQualification(AQuery<T> qualification, Query<? extends T> q) {
		MoCreateContext context = new MoCreateContext(this,q);
		qualification.setContext(context);
		context.createQuery(qualification, qualification, null);
	}

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
	
	private static class MoCreateContext implements ACreateContext {

		public MoCreateContext(MoManager moManager, Query<?> q) {
			// TODO Auto-generated constructor stub
		}

		public void createQuery(APrint p, AQuery<?> query, StringBuffer buffer) {

		}
		
	}
	
}
