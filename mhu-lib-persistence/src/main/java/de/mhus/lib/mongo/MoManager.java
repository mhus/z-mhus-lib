package de.mhus.lib.mongo;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.FindAndModifyOptions;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.NotSaved;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Serialized;
import org.mongodb.morphia.mapping.CustomMapper;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.mapping.cache.EntityCache;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.adb.Persistable;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.jmx.MJmx;
import de.mhus.lib.core.pojo.AttributesStrategy;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.core.pojo.PojoFilter;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoModelImpl;
import de.mhus.lib.core.pojo.PojoParser;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.errors.NotSupportedException;
import de.mhus.lib.sql.DbConnection;

public class MoManager extends MJmx implements MoHandler {

	private MongoClient client;
	private MoSchema schema;
	private Morphia morhia;
	private Datastore datastore;
	private LinkedList<Class<? extends Persistable>> managedTypes;
	private HashMap<String, PojoModel> modelCache = new HashMap<>();

	public MoManager(MongoClient client, MoSchema schema) {
		this.client = client;
		this.schema = schema;
		initDatabase();
	}

	@SuppressWarnings("rawtypes")
	protected void initDatabase() {
		Mapper mapper = createMapper();
		Set<Class> classObjs = new HashSet<>();
		managedTypes = new LinkedList<>();
		schema.findObjectTypes(managedTypes);
		classObjs.addAll(managedTypes);
		morhia = new Morphia(mapper, classObjs );
		datastore = morhia.createDatastore(client, schema.getDatabaseName());
		datastore.ensureIndexes();
	}
	
	protected Mapper createMapper() {
        Mapper mapper = new Mapper();

        mapper.getOptions().setValueMapper(createValueMapper(mapper));
        mapper.getOptions().setReferenceMapper(createReferenceMapper(mapper));
        mapper.getOptions().setEmbeddedMapper(createEmbeddedMapper(mapper));
        mapper.getOptions().setDefaultMapper(createCustomMapper(mapper));
        
        schema.initMapper(mapper);
        return mapper;
    }
	
	private CustomMapper createEmbeddedMapper(Mapper mapper) {
        MoCustomMapper m = new MoCustomMapper(mapper.getOptions().getEmbeddedMapper());
        return m;
	}

	private CustomMapper createReferenceMapper(Mapper mapper) {
        MoCustomMapper m = new MoCustomMapper(mapper.getOptions().getReferenceMapper());
        return m;
	}

	protected CustomMapper createValueMapper(Mapper mapper) {
        MoCustomMapper m = new MoCustomMapper(mapper.getOptions().getValueMapper());
        m.getIgnoreName().add("de.mhus.lib.adb.DbComfortableObject.persistent");
        m.getIgnoreName().add("de.mhus.lib.adb.DbComfortableObject.registryName");
        return m;
	}

	protected CustomMapper createCustomMapper(Mapper mapper) {
        MoCustomMapper m = new MoCustomMapper(mapper.getOptions().getDefaultMapper());
        m.getIgnoreName().add("de.mhus.lib.adb.DbComfortableObject.manager");
        m.getIgnoreName().add("de.mhus.lib.adb.DbComfortableObject.con");
        return m;
	}

	public void save(Object obj) {
		datastore.save(obj);
		if (obj instanceof DbComfortableObject)
			((DbComfortableObject)obj).doInit(this, null, true);
	}
	
	public void delete(Object obj) {
		datastore.delete(obj);
		if (obj instanceof DbComfortableObject)
			((DbComfortableObject)obj).doInit(null, null, false);
	}
	
	public <T> T getObject(Class<T> clazz, Object ... keys) throws MException {
		if (keys == null || keys.length != 1 || keys[0] == null) return null;
		return datastore.get(clazz, new ObjectId(String.valueOf(keys[0])));
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
	public boolean objectChanged(Object obj) throws MException {
		Object id = datastore.getKey(obj).getId();
		Object clone = datastore.get(obj.getClass(), id);
		try {
			PojoModel model = getModelFor(obj.getClass());
			for ( PojoAttribute<?> f : model) {
				Object v1 = f.get(obj);
				Object v2 = f.get(clone);
				if (!MSystem.equals(v1, v2)) return true;
			}
		} catch (IOException e) {
			throw new MException(e);
		}
		return false;
	}

	@Override
	public void reloadObject(DbConnection con, String registryName, Object obj) throws MException {
		Object id = datastore.getKey(obj).getId();
		Object clone = datastore.get(obj.getClass(), id);
		try {
			PojoModel model = getModelFor(obj.getClass());
			for ( PojoAttribute<Object> f : model) {
				Object v = f.get(clone);
				f.set(obj, v);
			}
		} catch (IOException e) {
			throw new MException(e);
		}
	}

	@Override
	public void deleteObject(DbConnection con, String registryName, Object obj) throws MException {
		delete(obj);
	}

	@Override
	public void createObject(DbConnection con, Object obj) throws MException {
		PojoModel model = getModelFor(obj.getClass());
		for ( PojoAttribute<?> f : model)
			if (f.getAnnotation(Id.class) != null) {
				try {
					f.set(obj, null);
				} catch (IOException e) {
					throw new MException(f,e);
				}
				break;
			}
		
		save(obj);
	}

	// -----
	
	public synchronized PojoModel getModelFor(Class<?> clazz) throws NotFoundException {
		// find managed object
		Class<? extends Persistable> type = null;
		for (Class<? extends Persistable> t : managedTypes) {
			if (clazz.isAssignableFrom(t)) {
				type = t;
				break;
			}
		}
		if (type == null) throw new NotFoundException("Managed type not found",clazz);
		
		PojoModel model = modelCache.get(type.getName());
		if (model == null) {
			model = new PojoParser().parse(type, new AttributesStrategy()).filter(new MongoFilter()).getModel();
			modelCache.put(type.getName(), model);
		}
		return model;
	}

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

	public Object getId(Object object) {
		if (object == null) return "";
		return datastore.getKey(object).getId();
	}

	public <T> T inject(T instance) {
		if (instance instanceof DbComfortableObject)
			((DbComfortableObject)instance).doInit(this, null, false);
		return instance;
	}
	
	private static class MongoFilter implements PojoFilter {

		@Override
		public void filter(PojoModelImpl model) {
			for (String name : model.getAttributeNames()) {
				PojoAttribute<?> attr = model.getAttribute(name);
				if (
						
						attr.getAnnotation(NotSaved.class) != null 
						||
						attr.getName().equals("persistent") && attr.getManagedClass() == DbComfortableObject.class
						||
						!attr.getType().isPrimitive() 
						&& 
						attr.getType() != String.class 
						&& 
						attr.getType() != Date.class
						&&
						!Map.class.isAssignableFrom(attr.getType())
						&&
						!List.class.isAssignableFrom(attr.getType())
						&&
						attr.getAnnotation(Property.class) == null 
						&& 
						attr.getAnnotation(Id.class) == null
						&&
						attr.getAnnotation(Serialized.class) == null
						&&
						attr.getAnnotation(Reference.class) == null
						&&
						attr.getAnnotation(Embedded.class) == null
						
						) {
					model.removeAttribute(name);
				}
			}
			
			for (String name : model.getActionNames()) {
				model.removeAction(name);
			}
			
		}
		
	}
	
	public class MoCustomMapper implements CustomMapper {

		private HashSet<String> ignoreName = new HashSet<>();
		private CustomMapper defaultMapper;

		public MoCustomMapper(CustomMapper defaultMapper) {
			this.defaultMapper = defaultMapper;
		}

		@Override
		public void fromDBObject(Datastore datastore, DBObject dbObject, MappedField mf, Object entity,
		        EntityCache cache, Mapper mapper) {

			// inject momanager
			if (entity instanceof DbComfortableObject)
				((DbComfortableObject)entity).doInit(MoManager.this, null, true);
			
			if (ignoreName.contains(mf.getFullName())) return;

			defaultMapper.fromDBObject(datastore, dbObject, mf, entity, cache, mapper);
		}

		@Override
		public void toDBObject(Object entity, MappedField mf, DBObject dbObject, Map<Object, DBObject> involvedObjects,
		        Mapper mapper) {
			
			if (ignoreName.contains(mf.getFullName())) return;
			
			defaultMapper.toDBObject(entity, mf, dbObject, involvedObjects, mapper);
		}

		public HashSet<String> getIgnoreName() {
			return ignoreName;
		}
		
	}
	
}
