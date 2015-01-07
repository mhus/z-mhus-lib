package de.mhus.lib.vaadin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

/**
 * Extend this Servlet to move the VAADIN folder as a local folder into the servlet context.
 * 
 * @author mikehummel
 *
 */
public abstract class VaadinLocalServlet extends  VaadinServlet {

	private static final long serialVersionUID = 1L;
	private String mappingPath = null;
	
	@Override
	protected void service(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

		if (handleContextRootWithoutSlash(request, response)) {
            return;
        }
        
        if (mappingPath == null) {
        	mappingPath = findMappingPath(request);
    		getService().getDeploymentConfiguration().getInitParameters().setProperty(PARAMETER_VAADIN_RESOURCES, mappingPath);
        }
        
		if (isLocalUIDL(request)) {
			request = new UidlRequestWrapper(request);
		} else
        if (isLocalStaticResourceRequest(request)) {
        	request = createLocalRequestMapper(request); 
        }
        
        super.service(request, response);
    }
    
    protected String findMappingPath(HttpServletRequest request) {
		return request.getServletPath() + request.getPathInfo();
	}

	protected HttpServletRequest createLocalRequestMapper(HttpServletRequest request) {
    	return  new ResourcesRequestWrapper(request, getMappingPath());
    }

	protected boolean isLocalStaticResourceRequest(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) return false;
        String uri = request.getRequestURI();
        if (uri != null && uri.startsWith( mappingPath + "/VAADIN/"))
        	return true;
        return false;
    }

	public String getMappingPath() {
		return mappingPath;
	}

	protected boolean isLocalUIDL(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) return false;
        String uri = request.getRequestURI();
        if (uri != null && uri.startsWith( getMappingPath() + "/UIDL/"))
        	return true;
        return false;
    }

    @Override
	protected VaadinServletService createServletService(
            DeploymentConfiguration deploymentConfiguration)
            throws ServiceException {
        VaadinServletService service = new LocalVaadinServletService(this,
                deploymentConfiguration);
        service.init();
        return service;
    }

	public String getServicePath() {
		return getMappingPath();
	}	
}
