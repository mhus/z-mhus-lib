package de.mhus.lib.portlet.actions;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.portlet.ActionRequest;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.errors.NotSupportedException;

public class ActionProperties extends AbstractProperties {

	ActionRequest request = null;
	
	public ActionProperties(ActionRequest request) {
		this.request = request;
	}

	@Override
	public Object getProperty(String name) {
		return request.getParameter(name);
	}

	@Override
	public boolean isProperty(String name) {
		return request.getParameter(name) != null;
	}

	@Override
	public void removeProperty(String key) {
	}

	@Override
	public void setProperty(String key, Object value) {
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public Set<String> keys() {
		return new HashSet<String>(Collections.list(request.getParameterNames()));
	}

	@Override
	public int size() {
		int cnt = 0;
		Enumeration<String> en = request.getParameterNames();
		while (en.hasMoreElements()) {
			en.nextElement();
			cnt++;
		}
		return cnt;
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

}
