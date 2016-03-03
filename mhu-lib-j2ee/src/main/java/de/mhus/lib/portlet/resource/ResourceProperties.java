package de.mhus.lib.portlet.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.portlet.ResourceRequest;

import de.mhus.lib.core.IProperties;

/**
 * <p>ResourceProperties class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ResourceProperties extends IProperties {

	ResourceRequest request = null;
	
	/**
	 * <p>Constructor for ResourceProperties.</p>
	 *
	 * @param request a {@link javax.portlet.ResourceRequest} object.
	 */
	public ResourceProperties(ResourceRequest request) {
		this.request = request;
	}

	/** {@inheritDoc} */
	@Override
	public Object getProperty(String name) {
		return request.getParameter(name);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isProperty(String name) {
		return request.getParameter(name) != null;
	}

	/** {@inheritDoc} */
	@Override
	public void removeProperty(String key) {
	}

	/** {@inheritDoc} */
	@Override
	public void setProperty(String key, Object value) {
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEditable() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public Set<String> keys() {
		return new HashSet<String>(Collections.list(request.getParameterNames()));
	}

}
