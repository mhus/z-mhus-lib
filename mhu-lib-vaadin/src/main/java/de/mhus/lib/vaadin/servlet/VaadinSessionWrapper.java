package de.mhus.lib.vaadin.servlet;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.util.MapEntry;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotSupportedException;

public class VaadinSessionWrapper extends AbstractProperties {

	private static final long serialVersionUID = 1L;
	private VaadinSession instance;

	public VaadinSessionWrapper(VaadinSession wrappedSession) {
		instance = wrappedSession;
	}

	@Override
	public boolean containsValue(Object value) {
		throw new NotSupportedException();
	}

	@Override
	public Collection<Object> values() {
		throw new NotSupportedException();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		throw new NotSupportedException();
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
		instance.setAttribute(key, null);
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
		throw new NotSupportedException();
	}

	@Override
	public int size() {
		throw new NotSupportedException();
	}

}