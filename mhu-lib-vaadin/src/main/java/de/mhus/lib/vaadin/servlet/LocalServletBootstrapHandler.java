package de.mhus.lib.vaadin.servlet;

import com.vaadin.server.communication.ServletBootstrapHandler;

public class LocalServletBootstrapHandler extends ServletBootstrapHandler {


	private VaadinLocalServlet servlet;

	public LocalServletBootstrapHandler(VaadinLocalServlet servlet) {
		this.servlet = servlet;
	}

	@Override
    protected String getServiceUrl(BootstrapContext context) {
//        String pathInfo = context.getRequest().getPathInfo();
//        if (pathInfo == null)
//        	return super.getServiceUrl(context);
        return servlet.getServicePath();
    }
}
