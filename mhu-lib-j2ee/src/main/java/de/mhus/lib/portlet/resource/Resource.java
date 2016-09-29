package de.mhus.lib.portlet.resource;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.IProperties;

/**
 * <p>Resource interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface Resource {

	/**
	 * <p>serveResource.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 * @param request a {@link javax.portlet.ResourceRequest} object.
	 * @param response a {@link javax.portlet.ResourceResponse} object.
	 * @return a boolean.
	 * @throws java.io.IOException if any.
	 * @throws javax.portlet.PortletException if any.
	 */
	public boolean serveResource(String path, ResourceRequest request,
			ResourceResponse response) throws IOException,
			PortletException;
	
	/**
	 * <p>createProperties.</p>
	 *
	 * @param request a {@link javax.portlet.ResourceRequest} object.
	 * @return a {@link de.mhus.lib.core.IProperties} object.
	 */
	public IProperties createProperties(ResourceRequest request);

}
