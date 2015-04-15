package de.mhus.lib.test.adb.model;

import java.util.HashMap;
import java.util.UUID;

import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.adb.DbDynamic;
import de.mhus.lib.adb.util.DynamicField;

public class Regal extends DbComfortableObject implements DbDynamic {

	private HashMap<String, Object> values = new HashMap<String, Object>();

	private static Field[] fields = new Field[] {
		new DynamicField("id",UUID.class,DynamicField.PRIMARY_KEY,"true","auto_id","true"),
		new DynamicField("store",UUID.class),
		new DynamicField("name",String.class),
		new DynamicField("room",int.class),
		new DynamicField("position",int.class)
	};

	@Override
	public Field[] getFieldDefinitions() {
		return fields;
	}

	@Override
	public void setValue(Field dynamicField, Object value) {
		setValue(dynamicField.getName(), value);
	}

	@Override
	public Object getValue(Field dynamicField) {
		return getValue(dynamicField.getName());
	}

	public void setValue(String name,Object value) {
		if (value == null)
			values.remove(name);
		else
			values.put(name,value);
	}

	public Object getValue(String name) {
		return values.get(name);
	}


}
