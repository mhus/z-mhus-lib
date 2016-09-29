package de.mhus.lib.portlet.resource;

import java.io.IOException;
import java.io.StringWriter;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;

public abstract class AjaxResource extends AbstractResource {

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

	protected boolean isDirectWriteEnabled() {
		return false;
	}

	protected abstract void doRequest(ResourceRequest request, JsonGenerator out) throws IOException,
	PortletException;
	
	
}
