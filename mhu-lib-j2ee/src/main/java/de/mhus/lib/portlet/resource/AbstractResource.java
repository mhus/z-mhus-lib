package de.mhus.lib.portlet.resource;

import javax.portlet.ResourceRequest;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.IProperties;

public abstract class AbstractResource implements Resource {

	@Override
	public synchronized IProperties createProperties(ResourceRequest request) {
		ResourceProperties properties = new ResourceProperties(request);
		return properties;
	}

}
