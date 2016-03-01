package de.mhus.lib.portlet.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.portlet.ResourceRequest;

import de.mhus.lib.core.AbstractProperties;

public class ResourceProperties extends AbstractProperties {

	ResourceRequest request = null;
	
	public ResourceProperties(ResourceRequest request) {
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
		return request.getParameterMap().size();
	}

}
