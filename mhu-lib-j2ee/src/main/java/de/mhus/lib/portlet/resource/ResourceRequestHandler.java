package de.mhus.lib.portlet.resource;

import java.io.IOException;
import java.util.HashMap;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.portlet.IllegalCharacterException;

/**
 * <p>ResourceRequestHandler class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ResourceRequestHandler implements Resource {

	private HashMap<String, Resource> registry = new HashMap<String, Resource>();
	
	/** {@inheritDoc} */
	@Override
	public boolean serveResource(String path, ResourceRequest request,
			ResourceResponse response) throws IOException,
			PortletException {
		
		if (path == null) return false;
		
		String name = null;
		int p = path.indexOf('/');
		if (p > 0) {
			name = path.substring(0,p);
			path = path.substring(p+1);
		} else {
			name = path;
			path = null;
		}
		Resource resource = null;
		synchronized (registry) {
			resource = registry.get(name);
		}
		if (resource == null) return false;
		
		return resource.serveResource(path, request, response);
	}
	
	/**
	 * <p>register.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param resource a {@link de.mhus.lib.portlet.resource.Resource} object.
	 */
	public void register(String name, Resource resource) {
		if (name == null || name.indexOf('/') >= 0) throw new IllegalCharacterException('/',name);
		synchronized (registry) {
			registry.put(name, resource);
		}
	}
	
	/**
	 * <p>unregister.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void unregister(String name) {
		synchronized (registry) {
			registry.remove(name);
		}
	}

	/**
	 * <p>serveResource.</p>
	 *
	 * @param resourceRequest a {@link javax.portlet.ResourceRequest} object.
	 * @param resourceResponse a {@link javax.portlet.ResourceResponse} object.
	 * @return a boolean.
	 * @throws java.io.IOException if any.
	 * @throws javax.portlet.PortletException if any.
	 */
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws IOException, PortletException {
		return serveResource(resourceRequest.getResourceID(), resourceRequest, resourceResponse);
	}

	/** {@inheritDoc} */
	@Override
	public synchronized IProperties createProperties(ResourceRequest request) {
		return null;
	}
	
	
	
}
