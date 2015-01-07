package de.mhus.lib.adb.util;

import de.mhus.lib.adb.DbDynamic;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;

public class DynamicField implements DbDynamic.Field {

	public static final String PRIMARY_KEY = "primary_key";
	public static final String INDEXES = "indexes";
	
	private String name;
	private boolean isPrimaryKey;
	private Class<?> ret;
	private ResourceNode attributes;
	private boolean persistent = true;
	
	public DynamicField() {}
	
	public DynamicField(String name, Class<?> ret, String ... attributes) {
		setName(name);
		setRet(ret);
		HashConfig x = new  HashConfig();
		for (int i = 0; i < attributes.length; i+=2) {
			try {
				x.setString(attributes[i], attributes[i+1]);
			} catch (MRuntimeException e) {}
		}
		setAttributes(x);
		setPrimaryKey(x.getBoolean(PRIMARY_KEY, false));
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	public void setRet(Class<?> ret) {
		this.ret = ret;
	}
	public Class<?> getReturnType() {
		return ret;
	}
	public void setAttributes(ResourceNode attributes) {
		this.attributes = attributes;
	}
	public ResourceNode getAttributes() {
		return attributes;
	}

	@Override
	public String[] getIndexes() throws MException {
		String v = attributes.getString(INDEXES,null);
		return v == null ? null : v.split(",");
	}

	public void setPersistent(boolean in) {
		persistent = in;
	}
	
	@Override
	public boolean isPersistent() {
		return persistent ;
	}
	
}
