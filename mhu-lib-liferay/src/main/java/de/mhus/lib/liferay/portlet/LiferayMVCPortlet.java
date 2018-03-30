/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.liferay.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.ParamUtil;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.portlet.actions.ActionRequestHandler;
import de.mhus.lib.portlet.resource.ResourceRequestHandler;

public abstract class LiferayMVCPortlet extends MVCPortlet {

	public static final String PUBLIC_PREFIX = "public_";

	public static final String REASON_ATTRIBUTE_NAME = "reason";

	public static final String INIT_PARAMETER_PUBLIC_VIEW = "public-view-jsp";
	
	protected ResourceRequestHandler resourcesHandler = new ResourceRequestHandler();
	protected ActionRequestHandler actionsHandler = new ActionRequestHandler();
	protected Log log = Log.getLog(this.getClass());
	/**
	 * Set this to true if the view jsp needs parameters from the request.
	 */
	protected boolean viewNeedParameters = false;

	private String publicViewTemplate;
	
	@Override
	public void init() throws PortletException {
		super.init();
		publicViewTemplate = getInitParameter(INIT_PARAMETER_PUBLIC_VIEW);
		doInit();
	}		

	protected abstract void doInit() throws PortletException;
	
	/**
	 * Return a reason why the user is not accepted. Return null if the user has rights.
	 * Overwrite the method to control access. The access will be controlled for edit mode and view mode.
	 * help mode is not affected. Resources and actions, starting with 'public_' will be allowed. Set the
	 * init parameter 'public-view-jsp' in the portlet configuration to configure a alternative
	 * jsp if access is restricted. The reason from hasRight() will be set in the request as attribute 'reason'. You
	 * can access it via JSP EL '${requestScope.reason}'.
	 *  
	 * @param request
	 * @return null by default.
	 */
	protected String hasRights(PortletRequest request) {
		return null;
	}
	
	
	@Override
	public void doView(RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {
		
		if (viewNeedParameters) {
			renderRequest = new RenderRequestWrapper(renderRequest);
		}
		
		String reason = hasRights(renderRequest);
		if (reason != null) {
			renderRequest.setAttribute(REASON_ATTRIBUTE_NAME, reason);
			doPublicView(renderRequest, renderResponse);
			return;
		}
		
		if (!doFillViewAttributes(renderRequest, renderResponse)) return;
		super.doView(renderRequest, renderResponse);
	}

	public void doPublicView(RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {
		if (publicViewTemplate != null)
			include(publicViewTemplate, renderRequest, renderResponse);
	}

	/**
	 * Overwrite this method if you need to give attributes to the jsp page. Alternatively you can
	 * cancel or forward the request by manipulating the response. If you want to deny execution of
	 * the jsp page, return false. Otherwise return true.
	 * @param request 
	 * @param response
	 * @return true for normal operation, false if no jsp page should be executed
	 */
	protected boolean doFillViewAttributes(RenderRequest request, RenderResponse response) throws IOException, PortletException {
		return true;
	}

	@Override
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {
		
		String actionName = ParamUtil.getString(request, ActionRequest.ACTION_NAME);
		if (hasRights(request) != null && !actionName.startsWith(PUBLIC_PREFIX)) return;

		log.t("action",actionName);
		try {
			if (MString.isSet(actionName) && actionsHandler.processAction(actionName,request, response)) return;
		} catch (Exception e) {
			throw new IOException("Action: " + actionName, e);
		}
		
		try {
			super.processAction(request, response);
		} catch (PortletException e) {
			log.e("Action exception",actionName,e.toString());
			throw e;
		}
	}
	
	@Override
	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {

		if (hasRights(resourceRequest) != null && !resourceRequest.getResourceID().startsWith(PUBLIC_PREFIX) ) {
			return;
		}

		log.t("resource",resourceRequest.getResourceID());
		if (resourcesHandler.serveResource(new ResourceRequestWrapper(resourceRequest), resourceResponse)) return;
		
		super.serveResource(resourceRequest, resourceResponse);
	}
	
	@Override
	public void doEdit(RenderRequest request, RenderResponse response) throws PortletException, java.io.IOException {
		if (hasRights(request) != null) return;
		super.doEdit(request, response);
	}


}
