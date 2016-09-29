package de.mhus.lib.core.pojo;

import java.util.HashMap;
import java.util.Iterator;

public class PojoModelImpl implements PojoModel {

	@SuppressWarnings("rawtypes")
	private HashMap<String, PojoAttribute> attributes = new HashMap<String, PojoAttribute>();
	private HashMap<String, PojoAction> actions = new HashMap<String, PojoAction>();
	private Class<?> clazz;
	
	public PojoModelImpl(Class<?> clazz ) {
		this.clazz = clazz;
	}
	
	public void addAttribute(@SuppressWarnings("rawtypes") PojoAttribute attr) {
		attributes.put(attr.getName(),attr);
	}

	public void addAction(PojoAction attr) {
		actions.put(attr.getName(),attr);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Iterator<PojoAttribute> iterator() {
		return attributes.values().iterator();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public PojoAttribute getAttribute(String name) {
		return attributes.get(name);
	}
	
	@Override
	public String[] getAttributeNames() {
		return attributes.keySet().toArray(new String[attributes.size()]);
	}

	@Override
	public Class<?> getManagedClass() {
		return clazz;
	}

	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}

	public void removeAction(String name) {
		actions.remove(name);
	}

	public boolean hasAction(String name) {
		return actions.containsKey(name);
	}
	
	@Override
	public PojoAction getAction(String name) {
		return actions.get(name);
	}

	@Override
	public String[] getActionNames() {
		return actions.keySet().toArray(new String[actions.size()]);
	}

}
