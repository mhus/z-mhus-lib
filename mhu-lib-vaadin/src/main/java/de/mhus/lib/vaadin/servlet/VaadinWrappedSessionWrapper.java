package de.mhus.lib.vaadin.servlet;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.vaadin.server.WrappedSession;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.util.MapEntry;
import de.mhus.lib.errors.NotSupportedException;

public class VaadinWrappedSessionWrapper extends AbstractProperties {

	private static final long serialVersionUID = 1L;
	private WrappedSession instance;

	public VaadinWrappedSessionWrapper(WrappedSession wrappedSession) {
		instance = wrappedSession;
	}

	@Override
	public boolean containsValue(Object value) {
		throw new NotSupportedException();
	}

	@Override
	public Collection<Object> values() {
		LinkedList<Object> out = new LinkedList<>();
		for (String name : instance.getAttributeNames()) {
			out.add(instance.getAttribute(name));
		}
		return out;
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		HashSet<Map.Entry<String, Object>> out = new HashSet<>();
		for (String name : instance.getAttributeNames()) {
			out.add(new MapEntry<String,Object>( name, instance.getAttribute(name)) );
		}
		return out;
	}

	@Override
	public Object getProperty(String name) {
		return instance.getAttribute(name);
	}

	@Override
	public boolean isProperty(String name) {
		return instance.getAttribute(name) != null;
	}

	@Override
	public void removeProperty(String key) {
		instance.removeAttribute(key);
	}

	@Override
	public void setProperty(String key, Object value) {
		instance.setAttribute(key, value);
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public Set<String> keys() {
		return instance.getAttributeNames();
	}

	@Override
	public int size() {
		return instance.getAttributeNames().size();
	}

	@Override
	public void clear() {
		for (String key : keys())
			remove(key);
	}

}