package de.mhus.lib.core.strategy;

import java.util.HashMap;
import java.util.Set;

public class SuccessfulMap extends Successful {

	public SuccessfulMap(Operation operation, String msg) {
		super(operation, msg);
		setResult(new HashMap<>());
	}

	public SuccessfulMap(String path, String msg, long rc) {
		super(path, msg, rc, new HashMap<>());
	}

	public SuccessfulMap(String path, String msg, long rc, String... keyValues) {
		super(path, msg, rc, keyValues);
	}

	@SuppressWarnings("unchecked")
	public void put(String key, Object value) {
		((HashMap<String,Object>)getResult()).put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public Object get(String key) {
		return ((HashMap<String,Object>)getResult()).get(key);
	}
	
	@SuppressWarnings("unchecked")
	public void remove(String key) {
		((HashMap<String,Object>)getResult()).remove(key);
	}

	@SuppressWarnings("unchecked")
	public Set<String> keySet() {
		return ((HashMap<String,String>)getResult()).keySet();
	}

	@SuppressWarnings("unchecked")
	public int size() {
		return ((HashMap<String,String>)getResult()).size();
	}
	
}
