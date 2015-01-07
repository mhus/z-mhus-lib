package de.mhus.lib.portlet.resource;

import java.io.IOException;
import java.util.HashMap;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.portlet.IllegalCharacterException;

public class ResourceRequestHandler implements Resource {

	private HashMap<String, Resource> registry = new HashMap<String, Resource>();
	
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
	
	public void register(String name, Resource resource) {
		if (name == null || name.indexOf('/') >= 0) throw new IllegalCharacterException('/',name);
		synchronized (registry) {
			registry.put(name, resource);
		}
	}
	
	public void unregister(String name) {
		synchronized (registry) {
			registry.remove(name);
		}
	}

	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws IOException, PortletException {
		return serveResource(resourceRequest.getResourceID(), resourceRequest, resourceResponse);
	}

	@Override
	public synchronized IProperties createProperties(ResourceRequest request) {
		return null;
	}
	
	
	
}
