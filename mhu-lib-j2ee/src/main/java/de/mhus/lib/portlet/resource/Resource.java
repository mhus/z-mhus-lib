package de.mhus.lib.portlet.resource;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.IProperties;

public interface Resource {

	public boolean serveResource(String path, ResourceRequest request,
			ResourceResponse response) throws IOException,
			PortletException;
	
	public IProperties createProperties(ResourceRequest request);

}
