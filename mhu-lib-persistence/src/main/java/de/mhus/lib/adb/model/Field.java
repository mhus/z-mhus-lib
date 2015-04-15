package de.mhus.lib.adb.model;

import java.util.HashMap;
import java.util.LinkedList;

import de.mhus.lib.adb.DbDynamic;
import de.mhus.lib.adb.DbManager;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.errors.MException;
import de.mhus.lib.sql.DbResult;

public abstract class Field extends MObject {

	protected boolean isPrimary;
	protected String name;
	protected String nameOrg;
	protected String createName;
	protected Table table;
	protected DbManager manager;
	protected boolean nullable = true;
	protected String defValue = null;
	protected int size = 200;
	protected String retDbType;
	protected String methodName;
	protected boolean autoId;
	protected ResourceNode attr;
	protected DbDynamic.Field dynamicField;
	protected PojoAttribute<Object> attribute;
	private LinkedList<AttributeFeature> features = new LinkedList<>();

	public abstract void prepareCreate(Object obj) throws Exception;
	public abstract boolean isPersistent();
	public abstract Object getFromTarget(Object obj) throws Exception;
	public abstract void setToTarget(DbResult res, Object obj) throws Exception;
	public abstract boolean changed(DbResult res, Object obj) throws Exception;
	public abstract void fillNameMapping(HashMap<String, Object> nameMapping);

	protected void init(String[] features) throws MException {
		if (features != null) {
			for (String featureName : features) {
				AttributeFeature f = manager.getSchema().createAttributeFeature(manager, this, featureName);
				if (f != null) this.features.add(f);
			}
		}
	}

	public void set(Object obj, Object value) throws Exception {

		if (attribute.getType().isEnum()) {
			int index = -1;
			if (value == null)
				index= MCast.toint(defValue, -1);
			else
				if (value instanceof Number)
					index = ((Number)value).intValue();

			Object[] values=attribute.getType().getEnumConstants();
			if (value instanceof String) {
				for (int i = 0; i < values.length; i++)
					if (values[i].toString().equals(value)) index = i;
				if (index < 0)
					index = MCast.toint(value, -1);
			}

			if (index < 0 || index >= values.length) throw new MException("index not found in enum",attribute.getType().getName());

			value = values[index];

		}

		for (Feature f : table.getFeatures())
			value = f.set(obj, this, value);

		for (AttributeFeature f : features)
			value = f.set(obj, value);

		if (dynamicField != null && obj instanceof DbDynamic)
			((DbDynamic)obj).setValue(dynamicField,value);
		else
			attribute.set(obj,value);
	}

	public boolean different(Object obj, Object value) throws Exception {

		if (attribute.getType().isEnum()) {
			int index = -1;
			if (value == null)
				index= MCast.toint(defValue, -1);
			else
				if (value instanceof Number)
					index = ((Number)value).intValue();

			Object[] values=attribute.getType().getEnumConstants();
			if (index < 0 || index >= values.length) throw new MException("index not found in enum",attribute.getType().getName());

			value = values[index];

		}

		for (Feature f : table.getFeatures())
			value = f.set(obj, this, value);

		for (AttributeFeature f : features)
			value = f.set(obj, value);

		Object objValue = null;

		if (dynamicField != null && obj instanceof DbDynamic)
			objValue = ((DbDynamic)obj).getValue(dynamicField);
		else
			objValue = attribute.get(obj);

		//		for (AttributeFeature f : features)
		//			objValue = f.get(obj, objValue);
		//
		//		for (Feature f : table.getFeatures())
		//			objValue = f.get(obj, this, objValue);

		return MSystem.equals(value, objValue);
	}

	public Object get(Object obj) throws Exception {
		Object val = null;
		if (dynamicField != null && obj instanceof DbDynamic)
			val = ((DbDynamic)obj).getValue(dynamicField);
		else
			val = attribute.get(obj);

		for (AttributeFeature f : features)
			val = f.get(obj, val);

		for (Feature f : table.getFeatures())
			val = f.get(obj, this, val);

		return val;
	}

	public ResourceNode getAttributes() {
		return attr;
	}
	public int getSize() {
		return size;
	}
	public Class<?> getType() {
		return attribute.getType();
	}

	public String getName() {
		return methodName;
	}

	public String getMappedName() {
		return name;
	}


}
