package de.mhus.lib.vaadin.servlet;

import java.util.List;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

public class LocalVaadinServletService extends VaadinServletService {

	public LocalVaadinServletService(VaadinServlet servlet,
			DeploymentConfiguration deploymentConfiguration)
			throws ServiceException {
		super(servlet, deploymentConfiguration);
	}

    @Override
    protected List<RequestHandler> createRequestHandlers()
            throws ServiceException {
        List<RequestHandler> handlers = super.createRequestHandlers();
        handlers.remove(0);
        handlers.add(0, new LocalServletBootstrapHandler( (VaadinLocalServlet)getServlet() ));
        return handlers;
    }

}
