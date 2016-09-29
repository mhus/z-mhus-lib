package de.mhus.lib.portlet.resource;

import javax.portlet.ResourceRequest;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.IProperties;

/**
 * <p>Abstract AbstractResource class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class AbstractResource implements Resource {

	/** {@inheritDoc} */
	@Override
	public synchronized IProperties createProperties(ResourceRequest request) {
		ResourceProperties properties = new ResourceProperties(request);
		return properties;
	}

}
