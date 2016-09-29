package de.mhus.lib.portlet.resource;

import java.io.IOException;
import java.io.StringWriter;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;

/**
 * <p>Abstract AjaxResource class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class AjaxResource extends AbstractResource {

	/** {@inheritDoc} */
	@Override
	public boolean serveResource(String path, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {

		JsonFactory f = new JsonFactory();

		if (isDirectWriteEnabled()) {
			resourceResponse.setContentType("application/json");
			resourceResponse.resetBuffer();
			resourceResponse.setCharacterEncoding("UTF-8");
			JsonGenerator out = f.createJsonGenerator(resourceResponse.getWriter());

			doRequest(resourceRequest, out);
			out.close();

			resourceResponse.flushBuffer();
		} else {
			StringWriter sw = new StringWriter();
			JsonGenerator out = f.createJsonGenerator(sw);
			
			doRequest(resourceRequest, out);
			out.close();
		
			resourceResponse.setContentType("application/json");
			resourceResponse.resetBuffer();
			resourceResponse.setCharacterEncoding("UTF-8");
			resourceResponse.getWriter().print(sw.toString());
			resourceResponse.flushBuffer();
		}
		return true;
	}

	/**
	 * <p>isDirectWriteEnabled.</p>
	 *
	 * @return a boolean.
	 */
	protected boolean isDirectWriteEnabled() {
		return false;
	}

	/**
	 * <p>doRequest.</p>
	 *
	 * @param request a {@link javax.portlet.ResourceRequest} object.
	 * @param out a {@link org.codehaus.jackson.JsonGenerator} object.
	 * @throws java.io.IOException if any.
	 * @throws javax.portlet.PortletException if any.
	 */
	protected abstract void doRequest(ResourceRequest request, JsonGenerator out) throws IOException,
	PortletException;
	
	
}
