package de.mhus.lib.adb.model;

import java.util.HashMap;

import de.mhus.lib.adb.DbDynamic;
import de.mhus.lib.core.MCast;
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
	protected boolean nullable = true;
	protected String defValue = null;
	protected int size = 200;
	protected String retDbType;
	protected String methodName;
	protected boolean autoId;
	protected ResourceNode attr;
	protected DbDynamic.Field dynamicField;
	protected PojoAttribute<Object> attribute;

	public abstract void prepareCreate(Object obj) throws Exception;
	public abstract boolean isPersistent();
	public abstract Object getFromTarget(Object obj) throws Exception;
	public abstract void setToTarget(DbResult res, Object obj) throws Exception;
	public abstract boolean changed(DbResult res, Object obj) throws Exception;
	public abstract void fillNameMapping(HashMap<String, Object> nameMapping);

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

		Object objValue = null;
		
		if (dynamicField != null && obj instanceof DbDynamic)
			objValue = ((DbDynamic)obj).getValue(dynamicField);
		else
			objValue = attribute.get(obj);
		
		if (value == null && objValue == null) return false;
		if (value == null) return true;
		return !value.equals(objValue);
		
	}

	public Object get(Object obj) throws Exception {
		Object val = null;
		if (dynamicField != null && obj instanceof DbDynamic)
			val = ((DbDynamic)obj).getValue(dynamicField);
		else
			val = attribute.get(obj);
		
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
