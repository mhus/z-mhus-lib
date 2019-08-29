/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.mongo;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;

import com.mongodb.MongoClient;

import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.adb.Persistable;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.annotations.pojo.Embedded;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.jmx.MJmx;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.pojo.AttributesStrategy;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.core.pojo.PojoFilter;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoModelImpl;
import de.mhus.lib.core.pojo.PojoParser;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.sql.DbConnection;
import dev.morphia.Datastore;
import dev.morphia.FindAndModifyOptions;
import dev.morphia.Morphia;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.NotSaved;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Reference;
import dev.morphia.annotations.Serialized;
import dev.morphia.mapping.Mapper;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateOperations;
import dev.morphia.query.UpdateResults;

@SuppressWarnings("deprecation")
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
        Mapper mapper = new MoMapper(this);
        
        schema.initMapper(mapper);
        return mapper;
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

	@SuppressWarnings("unchecked")
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
			if (f.getAnnotation(DbPrimaryKey.class) != null) {
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
						attr.getManagedClass() == DbComfortableObject.class
						||
						attr.getManagedClass() == MObject.class
						||
						!attr.getType().isPrimitive() 
						&& 
						attr.getType() != String.class 
						&& 
						attr.getType() != Date.class
						&&
						attr.getType() != BigDecimal.class
						&&
						attr.getType() != BigInteger.class
						&&
						!Map.class.isAssignableFrom(attr.getType())
						&&
						!List.class.isAssignableFrom(attr.getType())
						&&
						attr.getAnnotation(Property.class) == null 
						&& 
						attr.getAnnotation(Serialized.class) == null
						&&
						attr.getAnnotation(Reference.class) == null
						&&
						attr.getAnnotation(Embedded.class) == null
						&&
                        attr.getAnnotation(DbPrimaryKey.class) == null
                        &&
                        attr.getAnnotation(DbPersistent.class) == null
						) {
					model.removeAttribute(name);
				}
			}
			
			for (String name : model.getActionNames()) {
				model.removeAction(name);
			}
			
		}
		
	}
		
}
