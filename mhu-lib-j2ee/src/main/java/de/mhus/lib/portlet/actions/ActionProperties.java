package de.mhus.lib.portlet.actions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.portlet.ActionRequest;

import de.mhus.lib.core.IProperties;

public class ActionProperties extends IProperties {

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

}
