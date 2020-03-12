/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

/**
 * MPortlet class.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MPortlet extends GenericPortlet {

    protected ResourceRequestHandler resourcesHandler = new ResourceRequestHandler();
    protected ActionRequestHandler actionsHandler = new ActionRequestHandler();
    protected Log log = Log.getLog(this.getClass());

    protected String editJsp;
    protected String viewJsp;

    /** {@inheritDoc} */
    @Override
    public void init() throws PortletException {
        editJsp = getInitParameter("edit-template");
        viewJsp = getInitParameter("view-template");
    }

    /** {@inheritDoc} */
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

        include(editJsp, request, response);
    }

    /** {@inheritDoc} */
    @Override
    protected void doView(RenderRequest request, RenderResponse response)
            throws PortletException, IOException {

        PortletPreferences prefs = request.getPreferences();
        String userName = (String) prefs.getValue("name", "");
        request.setAttribute("name", userName);
        include(viewJsp, request, response);
    }

    /**
     * include.
     *
     * @param path a {@link java.lang.String} object.
     * @param request a {@link javax.portlet.RenderRequest} object.
     * @param response a {@link javax.portlet.RenderResponse} object.
     * @throws javax.portlet.PortletException if any.
     * @throws java.io.IOException if any.
     */
    protected void include(String path, RenderRequest request, RenderResponse response)
            throws PortletException, IOException {

        PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher(path);
        if (dispatcher == null) log.e("include", "path not found", path);
        else dispatcher.include(request, response);
    }

    /** {@inheritDoc} */
    @Override
    public void processAction(ActionRequest request, ActionResponse response)
            throws PortletException, IOException {

        log.t("action", request.getParameter(ActionRequest.ACTION_NAME));
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

    /** {@inheritDoc} */
    @Override
    public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
            throws IOException, PortletException {

        log.t("resource", resourceRequest.getResourceID());
        if (resourcesHandler.serveResource(resourceRequest, resourceResponse)) return;

        super.serveResource(resourceRequest, resourceResponse);
    }
}
