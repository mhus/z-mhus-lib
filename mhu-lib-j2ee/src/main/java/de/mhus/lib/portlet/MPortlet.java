package de.mhus.lib.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import de.mhus.lib.core.logging.Log;
import de.mhus.lib.portlet.actions.ActionRequestHandler;
import de.mhus.lib.portlet.resource.ResourceRequestHandler;

public class MPortlet extends GenericPortlet {

	protected ResourceRequestHandler resourcesHandler = new ResourceRequestHandler();
	protected ActionRequestHandler actionsHandler = new ActionRequestHandler();
	protected Log log = Log.getLog(this.getClass());

	protected String editJsp;
	protected String viewJsp;

	@Override
	public void init() throws PortletException {
		editJsp = getInitParameter("edit-template");
		viewJsp = getInitParameter("view-template");
	}

	@Override
	protected void doEdit(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		
		response.setContentType("text/html");
		PortletURL addNameUrl = response.createActionURL();
		addNameUrl.setParameter("action", "addName");
		request.setAttribute("addNameUrl", addNameUrl.toString());

		PortletPreferences prefs = request.getPreferences();
		String userName = (String) prefs.getValue("name", "");
		request.setAttribute("name", userName);
		
		include(editJsp,request,response);
	}

	@Override
	protected void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {

		PortletPreferences prefs = request.getPreferences();
		String userName = (String) prefs.getValue("name", "");
		request.setAttribute("name", userName);
		include(viewJsp,request,response);
	}

	protected void include(String path, RenderRequest request,
			RenderResponse response) throws PortletException, IOException {

		PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher(path);
		if (dispatcher == null)
			log.e("include","path not found",path);
		else
			dispatcher.include(request, response);
	}

	@Override
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {
		
		log.t("action",request.getParameter(ActionRequest.ACTION_NAME));
		try {
			if (actionsHandler.processAction(request, response)) return;
		} catch (PortletException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
		super.processAction(request, response);
	}
	
	@Override
	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {
				
		log.t("resource",resourceRequest.getResourceID());
		if (resourcesHandler.serveResource(resourceRequest, resourceResponse)) return;
		
		super.serveResource(resourceRequest, resourceResponse);
	}

}
