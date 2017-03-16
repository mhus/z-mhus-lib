package de.mhus.lib.vaadin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;

import com.vaadin.server.VaadinServlet;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.MConfigFactory;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.logging.Log;

public class MVaadinServlet extends VaadinServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Log log = Log.getLog(MVaadinServlet.class);
	
	private ResourceNode config;

    @Override
    public void init() throws ServletException {
        super.init();
        
        // Load an config file
        String mhusConfigPath = getServletConfig().getInitParameter("mhus.config");
        URL mhusConfigUrl = null;
        if (mhusConfigPath != null) {
        	try {
				mhusConfigUrl = new File(mhusConfigPath).toURI().toURL();
			} catch (MalformedURLException e) {
				log.i(mhusConfigPath,e);
			}
        }
        if (mhusConfigUrl == null)
			try {
				mhusConfigUrl = MSystem.locateResource(this, getApplicationConfigName());
			} catch (IOException e) {
				log.i(getApplicationConfigName(),e);
			}
        if (mhusConfigUrl != null)
	        try {
	        	config = MApi.lookup(MConfigFactory.class).createConfigFor(mhusConfigUrl.toURI());
	        } catch (Exception e) {
	        	log.i(mhusConfigPath,e);
	        }
        else
        	config = new HashConfig();
        
        
        doInit();
        
    }
    
    /**
     * Use this method to initialize extended classes.
     */
	protected void doInit() {
		
	}

	protected String getApplicationConfigName() {
		return "config.xml";
	}


	public ResourceNode getConfig() {
		return config;
	}

    @Override
	public void destroy() {
    	super.destroy();
    }

//	public void setConfig(IConfig config) {
//		this.config = config;
//	}
	
}
