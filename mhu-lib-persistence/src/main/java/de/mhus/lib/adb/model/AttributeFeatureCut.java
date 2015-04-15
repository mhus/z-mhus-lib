package de.mhus.lib.adb.model;

import de.mhus.lib.adb.DbManager;

public class AttributeFeatureCut implements AttributeFeature {

	//	private Field field;
	private int size;

	@Override
	public void init(DbManager manager, Field field) {
		//		this.field = field;
		size = field.getSize();
	}

	@Override
	public Object set(Object pojo, Object value) {
		if (value != null && value instanceof String && ((String)value).length() > size)
			value = ((String)value).substring(0, size);
		return value;
	}

	@Override
	public Object get(Object pojo, Object value) {
		return value;
	}

}
