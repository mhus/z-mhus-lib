package de.mhus.lib.adb.model;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.DbSchema;
import de.mhus.lib.annotations.adb.DbIndex;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.annotations.adb.DbTable;
import de.mhus.lib.annotations.adb.DbType;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.MConfigFactory;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.directory.WritableResourceNode;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.lang.Raw;
import de.mhus.lib.core.util.Rfc1738;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DbPrepared;
import de.mhus.lib.sql.DbResult;
import de.mhus.lib.sql.Dialect;

public abstract class Table extends MObject {

	protected Class<?> clazz;
	protected String registryName;
	protected DbManager manager;
	protected String name;
	protected String tableNameOrg;
	protected DbSchema schema;
	protected String tableName;
	protected HashMap<String,Field> fIndex = new HashMap<String, Field>();
	protected LinkedList<Field> fList = new LinkedList<Field>();
	protected LinkedList<FieldRelation> relationList = new LinkedList<FieldRelation>();
	protected HashMap<String,FieldRelation> relationIndex = new HashMap<String, FieldRelation>();
	private HashMap<String, LinkedList<Field>> iIdx = new HashMap<String, LinkedList<Field>>();
	protected LinkedList<Field> pk = new LinkedList<Field>();
	private DbPrepared sqlPrimary;
	private DbPrepared sqlInsert;
	private DbPrepared sqlUpdate;
	private DbPrepared sqlUpdateForce;
	private DbPrepared sqlDelete;
	private LinkedList<Feature> features = new LinkedList<Feature>();
	protected ResourceNode attributes;

	public void init(DbManager manager, Class<?> clazz, String registryName, String tableName) {
		this.manager = manager;
		this.schema = manager.getSchema();
		this.clazz = clazz;
		this.registryName = registryName;
		this.tableName = tableName;
	}

	public void initDatabase(DbConnection con, boolean cleanup) throws Exception {

		DbTable table = MSystem.findAnnotation(clazz, DbTable.class);
		if (tableName != null) {
			name = tableName;
		} else
			if (table == null || MString.isEmptyTrim(table.tableName())) {
				name = clazz.getSimpleName();
			} else {
				name = table.tableName();
			}

		if (table != null &&!MString.isEmptyTrim(table.attributes())) {
			attributes = base(MConfigFactory.class).toConfig(table.attributes());
		} else {
			attributes = new HashConfig();
		}

		tableNameOrg = schema.getTableName(name);
		tableName = manager.getPool().getDialect().normalizeTableName(tableNameOrg);

		log().t("new table",name,tableName);

		parseFields();

		// features
		if (table != null) {
			for (String featureName : table.features()) {
				Feature feature = manager.getSchema().createFeature(manager, this, featureName);
				if (feature != null) features.add(feature);
			}
		}

		createTable(con, cleanup);
		postInit();
	}

	protected abstract void parseFields() throws Exception;


	protected void addToIndex(String[] list, Field field) {
		for (String nr : list) {
			LinkedList<Field> list2 = iIdx.get(nr);
			if (list2 == null) {
				list2 = new LinkedList<Field>();
				iIdx.put(nr, list2);
			}
			list2.add(field);
		}
	}

	protected void addField(FieldRelation field) {
		relationList.add(field);
		relationIndex.put(field.getName(), field);
	}

	public FieldRelation getFieldRelation(String name) {
		return relationIndex.get(name);
	}

	protected void addField(Field field) {
		field.table = this;
		fIndex.put(field.createName, field);
		fList.add(field);
		if (field.isPrimary && field.isPersistent()) pk.add(field);
	}

	public void fillNameMapping(HashMap<String, Object> nameMapping) throws Exception {
		nameMapping.put("db." + manager.getMappingName(clazz), new Raw(tableName));
		for (Field f : fList) {
			f.fillNameMapping(nameMapping);
		}
	}

	public void prepareCreate(Object object) throws Exception {
		for (Field f : fList) {
			f.prepareCreate(object);
		}
		for (FieldRelation f : relationList) {
			f.prepareCreate(object);
		}
	}

	public void createObject(DbConnection con, Object object) throws Exception {

		for (Feature f : features)
			f.preCreateObject(con,object);

		HashMap<String, Object> attributes = new HashMap<String, Object>();
		for (Field f : fList) {
			attributes.put(f.name, f.getFromTarget(object));
		}

		schema.internalCreateObject(con, name, object, attributes);

		sqlInsert.getStatement(con).execute(attributes);

		for (Feature f : features)
			f.postCreateObject(con,object);

		for (FieldRelation f : relationList) {
			f.created(con,object);
		}

	}

	public void saveObject(DbConnection con, Object object) throws Exception {

		for (Feature f : features)
			f.preSaveObject(con,object);

		HashMap<String, Object> attributes = new HashMap<String, Object>();
		for (Field f : fList) {
			attributes.put(f.name, f.getFromTarget(object));
		}

		for (FieldRelation f : relationList) {
			f.prepareSave(con,object);
		}

		schema.internalSaveObject(con, name, object, attributes);

		int c = sqlUpdate.getStatement(con).executeUpdate(attributes);
		if ( c != 1)
			throw new MException("update failed, updated objects " + c);

		for (Feature f : features)
			f.postSaveObject(con,object);
		
		for (FieldRelation f : relationList) {
			f.saved(con,object);
		}

	}

	public void saveObjectForce(DbConnection con, Object object, boolean raw) throws Exception {

		manager.getSchema().authorizeSaveForceAllowed(con, this, object, raw);
		
		if (!raw)
			for (Feature f : features)
				f.preSaveObject(con,object);

		HashMap<String, Object> attributes = new HashMap<String, Object>();
		for (Field f : fList) {
			attributes.put(f.name, f.getFromTarget(object));
		}

		for (FieldRelation f : relationList) {
			f.prepareSave(con,object);
		}

		schema.internalSaveObject(con, name, object, attributes);

		int c = sqlUpdateForce.getStatement(con).executeUpdate(attributes);
		if ( c != 1)
			throw new MException("update failed, updated objects " + c);

		if (!raw)
			for (Feature f : features)
				f.postSaveObject(con,object);
		
		for (FieldRelation f : relationList) {
			f.saved(con,object);
		}

	}
	
	public void updateAttributes(DbConnection con, Object object, boolean raw, String ... attributeNames ) throws Exception {
		
		manager.getSchema().authorizeUpdateAttributes(con, this, object, raw, attributeNames);

		HashMap<String, Object> attributes = new HashMap<String, Object>();
		
		// prepare object
		if (!raw)
			for (Feature f : features)
				f.preSaveObject(con,object);

		// create query and collect values
		
		StringBuffer sql = new StringBuffer().append("UPDATE ").append(tableName).append(" SET ");
		int nr = 0;
		for (String aname : attributeNames) {
			Field f = fIndex.get(aname);
			if (f == null)
				throw new NotFoundException("field not found", name, aname);
				
			if (!f.isPrimary && f.isPersistent()) {
				if (nr > 0) sql.append(",");
				sql.append(f.name).append("=$").append(f.name).append("$");
				nr++;
				attributes.put(f.name,  f.getFromTarget(object)); // collect values
			}
		}
		if (nr == 0)
			throw new NotFoundException("no valid fields found");
			
		sql.append(" WHERE ");
		nr = 0;
		for (Field f : pk) {
			sql.append( (nr > 0 ? " AND " : "" ) ).append(f.name).append("=$").append(f.name).append("$");
			nr++;
			attributes.put(f.name, f.getFromTarget(object)); // collect values
		}

		DbPrepared query = manager.getPool().createStatement(sql.toString());
		
		// execute query 

		// not needed - object itself is not saved
//		for (FieldRelation f : relationList) {
//			f.prepareSave(con,object);
//		}

		schema.internalSaveObject(con, name, object, attributes);

		int c = query.getStatement(con).executeUpdate(attributes);
		if ( c != 1)
			throw new MException("update failed, updated objects " + c);

		if (!raw)
			for (Feature f : features)
				f.postSaveObject(con,object);
		
		// not needed - object itself is not saved
//		for (FieldRelation f : relationList) {
//			f.saved(con,object);
//		}

	}
	
	protected void postInit() throws MException {

		Collections.sort(pk, new Comparator<Field>() {

			@Override
			public int compare(Field o1, Field o2) {
				return o1.name.compareTo(o2.name);
			}
		});

		String sql = "SELECT * FROM " + tableName + " WHERE ";
		int nr = 0;
		for (Field f : pk) {
			sql+= (nr > 0 ? " AND " : "" ) + f.name + "=$" + nr + "$";
			nr++;
		}
		// TODO dialect.appendSqlLimit(0,1)

		sqlPrimary = manager.getPool().createStatement(sql);

		// ------

		sql = "INSERT INTO " + tableName + " (";
		nr = 0;
		for (Field f : fList) {
			if (f.isPersistent()) {
				if (nr > 0) sql += ",";
				sql += f.name;
				nr++;
			}
		}
		sql += ") VALUES (";
		nr = 0;
		for (Field f : fList) {
			if (f.isPersistent()) {
				if (nr > 0) sql += ",";
				sql += "$" + f.name + "$";
				nr++;
			}
		}
		sql += ")";

		sqlInsert = manager.getPool().createStatement(sql);

		// ------

		sql = "UPDATE " + tableName + " SET ";
		nr = 0;
		for (Field f : fList) {
			if (!f.isPrimary && f.isPersistent() && !f.isReadOnly()) {
				if (nr > 0) sql += ",";
				sql += f.name + "=$" + f.name + "$";
				nr++;
			}
		}
		sql+= " WHERE ";
		nr = 0;
		for (Field f : pk) {
			sql+= (nr > 0 ? " AND " : "" ) + f.name + "=$" + f.name + "$";
			nr++;
		}

		sqlUpdate = manager.getPool().createStatement(sql);

		// ------

		sql = "UPDATE " + tableName + " SET ";
		nr = 0;
		for (Field f : fList) {
			if (!f.isPrimary && f.isPersistent() ) {
				if (nr > 0) sql += ",";
				sql += f.name + "=$" + f.name + "$";
				nr++;
			}
		}
		sql+= " WHERE ";
		nr = 0;
		for (Field f : pk) {
			sql+= (nr > 0 ? " AND " : "" ) + f.name + "=$" + f.name + "$";
			nr++;
		}

		sqlUpdateForce = manager.getPool().createStatement(sql);

		// ------

		sql = "DELETE FROM " + tableName + " WHERE ";
		nr = 0;
		for (Field f : pk) {
			sql+= (nr > 0 ? " AND " : "" ) + f.name + "=$" + f.name + "$";
			nr++;
		}

		sqlDelete = manager.getPool().createStatement(sql);

	}

	public Object getObject(DbConnection con, Object[] keys) throws Exception {

		HashMap<String, Object> attributes = new HashMap<String, Object>();
		int nr = 0;
		for (Object key : keys) {
			attributes.put(String.valueOf(nr), key);
			nr++;
		}
		DbResult ret = sqlPrimary.getStatement(con).executeQuery(attributes);
		if (!ret.next()) {
			ret.close();
			return null;
		}

		for (Feature f : features)
			f.preGetObject(con,ret);

		Object obj = schema.createObject(clazz, registryName, ret,manager, true);

		// fill object
		for (Field f : fList) {
			f.setToTarget(ret,obj);
		}

		for (Feature f : features)
			f.postGetObject(con,obj);

		for (FieldRelation f : relationList) {
			f.loaded(con,obj);
		}

		return obj;
	}

	public boolean existsObject(DbConnection con, Object[] keys) throws Exception {

		HashMap<String, Object> attributes = new HashMap<String, Object>();
		int nr = 0;
		for (Object key : keys) {
			attributes.put(String.valueOf(nr), key);
			nr++;
		}
		DbResult ret = sqlPrimary.getStatement(con).executeQuery(attributes);
		if (!ret.next()) {
			ret.close();
			return false;
		}
		ret.close();

		return true;
	}
	public void injectObject(Object obj) {
		if (obj instanceof DbComfortableObject) // TODO Use DbObject interface
			((DbComfortableObject)obj).setDbManager(manager);
		for (FieldRelation f : relationList) {
			f.inject(obj);
		}
	}

	public void fillObject(Object obj, DbConnection con, DbResult res) throws Throwable {

		for (Feature f : features)
			f.preFillObject(obj,con,res);

		for (Field f : fList) {
			try {
				f.setToTarget(res,obj);
			} catch (Throwable t) {
				manager.getSchema().onFillObjectException(Table.this,obj, res, f, t);
			}
		}

		for (Feature f : features)
			f.postFillObject(obj,con);
		
		for (FieldRelation f : relationList) {
			f.loaded(con,obj);
		}

	}

	public Object fillObject(DbConnection con, Object obj, Object[] keys) throws Throwable {

		HashMap<String, Object> attributes = new HashMap<String, Object>();
		int nr = 0;
		for (Object key : keys) {
			attributes.put(String.valueOf(nr), key);
			nr++;
		}
		DbResult ret = sqlPrimary.getStatement(con).executeQuery(attributes);
		if (!ret.next()) {
			ret.close();
			return null;
		}

		for (Feature f : features)
			f.preFillObject(obj, con, ret);

		// fill object
		for (Field f : fList) {
			try {
				f.setToTarget(ret,obj);
			} catch (Throwable t) {
				manager.getSchema().onFillObjectException(Table.this, obj, ret, f, t);
			}
		}
		ret.close();

		for (Feature f : features)
			f.postFillObject(obj, con);
		
		for (FieldRelation f : relationList) {
			f.loaded(con,obj);
		}


		return obj;
	}

	public boolean objectChanged(DbConnection con, Object obj, Object[] keys) throws Exception {

		for (FieldRelation field : relationList) {
			if (field.isChanged(obj)) return true;
		}

		HashMap<String, Object> attributes = new HashMap<String, Object>();
		int nr = 0;
		for (Object key : keys) {
			attributes.put(String.valueOf(nr), key);
			nr++;
		}
		DbResult ret = sqlPrimary.getStatement(con).executeQuery(attributes);
		if (!ret.next()) {
			ret.close();
			return true;
		}

		//		for (Feature f : features)
		//			f.fillObject(obj, con, ret);

		// check object
		for (Field f : fList) {
			if (f.changed(ret,obj)) {
				ret.close();
				return true;
			}
		}
		ret.close();
		return false;
	}

	/**
	 * Create the dables in the database.
	 * 
	 * @param con
	 * @param cleanup 
	 * @throws Exception
	 */
	public void createTable(DbConnection con, boolean cleanup) throws Exception {

		HashConfig cstr = new HashConfig();
		WritableResourceNode ctable = cstr.createConfig("table");
		ctable.setProperty("name", tableNameOrg);

		LinkedList<String> pk = new LinkedList<String>();

		for (Field f : fList) {
			ResourceNode cfield = ctable.createConfig("field");
			cfield.setProperty(Dialect.K_NAME, f.createName);
			cfield.setProperty(Dialect.K_TYPE, f.retDbType);
			cfield.setProperty(Dialect.K_SIZE, String.valueOf(f.size));
			cfield.setProperty(Dialect.K_DEFAULT, f.defValue);
			cfield.setProperty(Dialect.K_NOT_NULL, f.nullable ? "no" : "yes");
			cfield.setProperty(Dialect.K_DESCRIPTION, f.description);
			cfield.setProperty(Dialect.K_HINTS, Rfc1738.implodeArray(f.hints));
			LinkedList<String> cat = new LinkedList<String>();
			if (!f.isPersistent()) cat.add(Dialect.C_VIRTUAL);
			if (f.isPrimary) cat.add(Dialect.C_PRIMARY_KEY);
			if (f.getType().isEnum()) cat.add(Dialect.C_ENUMERATION);
			cfield.setProperty(Dialect.K_CATEGORIES, MString.join(cat.iterator(), ",") );  // add primary key
			if (f.isPrimary && f.isPersistent()) pk.add(f.createName);
		}

		if (pk.size() > 0) {
			String pkNames = MString.join(pk.iterator(), ",");
			ctable.setProperty(Dialect.K_PRIMARY_KEY, pkNames);
		}

		// create index entries
		for (Entry<String, LinkedList<Field>> item : iIdx.entrySet()) {
			ResourceNode cindex = cstr.createConfig("index");
			String n = item.getKey();
			if (n.startsWith(DbIndex.UNIQUE)) {
				cindex.setString(Dialect.I_TYPE, Dialect.I_UNIQUE);
				cindex.setBoolean(Dialect.I_UNIQUE, true);
			}
			cindex.setString(Dialect.I_NAME, "idx_" + n);
			cindex.setString(Dialect.I_TABLE, tableNameOrg);
			StringBuffer fields = new StringBuffer();
			for (Field field : item.getValue()) {
				if (fields.length() > 0) fields.append(",");
				fields.append(field.createName);
			}
			cindex.setString(Dialect.I_FIELDS, fields.toString());
		}

		manager.getPool().getDialect().createStructure(cstr, con, manager.getCaoMetadata(), cleanup);

	}

	public void deleteObject(DbConnection con, Object object) throws Exception {

		for (Feature f : features)
			f.deleteObject(con,object);

		HashMap<String, Object> attributes = new HashMap<String, Object>();
		for (Field f : pk) {
			attributes.put(f.name, f.getFromTarget(object));
		}

		schema.internalDeleteObject(con, name, object, attributes);

		sqlDelete.getStatement(con).execute(attributes);
	}

	public String getRegistryName() {
		return registryName;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String getTableName() {
		return tableNameOrg;
	}

	public Field getField(String fName) {
		return fIndex.get(fName);
	}

	public List<Field> getPrimaryKeys() {
		return pk;
	}

	public String toAttributes(DbPersistent pa, DbPrimaryKey pk) {

		if (pa == null && pk == null) return "";

		StringBuffer out = new StringBuffer();

		if (pa != null) out.append("size=").append(pa.size());

		if ( (pa != null && pa.auto_id()) || (pk != null && pk.auto_id()) )
			out.append("&auto_id=true");

		if (pa != null && !pa.nullable())
			out.append("&nullable=false");

		if (pa != null)  {
			String type = Dialect.typeEnumToString(pa.type());
			if (!MString.isEmpty(type))
				out.append("&type=").append(type);

			out.append("&description=").append(Rfc1738.encode(pa.description()));
			if (pa.hints().length > 0)
				out.append("&hints=").append(Rfc1738.encode(Rfc1738.implodeArray(pa.hints())));
			
			String more = pa.more();
			if (!MString.isEmpty(more))
				out.append("&").append(more);
			
			
		}
		return out.toString();
	}

	protected String getDbRetType(Class<?> ret) {
		String rt = DbType.TYPE.BLOB.name();
		if (ret == int.class) rt = DbType.TYPE.INT.name();
		else
			if (ret == long.class) rt = DbType.TYPE.LONG.name();
			else
				if (ret == boolean.class) rt = DbType.TYPE.BOOL.name();
				else
					if (ret == double.class) rt = DbType.TYPE.DOUBLE.name();
					else
						if (ret == float.class) rt = DbType.TYPE.FLOAT.name();
						else
							if (ret == String.class) rt = DbType.TYPE.STRING.name();
							else
								if (ret == Date.class || ret == Calendar.class || ret == java.sql.Date.class) rt = DbType.TYPE.DATETIME.name();
								else
									if (ret == UUID.class) rt = DbType.TYPE.UUID.name();
									else
										if (ret.isEnum()) rt = DbType.TYPE.INT.name();

		return rt;
	}

	public String getName() {
		return name;
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public ResourceNode getAttributes() {
		return attributes;
	}

	public Field[] getFields() {
		return fList.toArray(new Field[fList.size()]);
	}

	public FieldRelation[] getFieldRelations() {
		return relationList.toArray(new FieldRelation[fList.size()]);
	}

}
