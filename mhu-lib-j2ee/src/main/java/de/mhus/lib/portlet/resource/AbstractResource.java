package de.mhus.lib.portlet.resource;

import javax.portlet.ResourceRequest;

import de.mhus.lib.core.AbstractProperties;

public abstract class AbstractResource implements Resource {

	@Override
	public synchronized AbstractProperties createProperties(ResourceRequest request) {
		ResourceProperties properties = new ResourceProperties(request);
		return properties;
	}

}
