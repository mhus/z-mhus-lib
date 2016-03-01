package de.mhus.lib.portlet.actions;

import java.util.HashMap;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.portlet.IllegalCharacterException;

public class ActionRequestHandler implements Action {

	private HashMap<String, Action> registry = new HashMap<String, Action>();
	
	@Override
	public boolean processAction(String path, ActionRequest request, ActionResponse response)
			throws Exception {

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
		Action resource = null;
		synchronized (registry) {
			resource = registry.get(name);
		}
		if (resource == null) return false;
		
		return resource.processAction(path, request, response);
	}
	
	public void register(String name, Action resource) {
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

	public boolean processAction(ActionRequest request, ActionResponse response)
			throws Exception {
		String action = request.getParameter(ActionRequest.ACTION_NAME);
		if (action != null) {
			return processAction(action,request,response);
		}
		return false;
	}

	@Override
	public synchronized AbstractProperties createProperties(ActionRequest request) {
		return null;
	}

}
