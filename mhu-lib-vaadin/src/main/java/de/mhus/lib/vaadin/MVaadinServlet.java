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
package de.mhus.lib.vaadin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;

import com.vaadin.server.VaadinServlet;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.MConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.logging.Log;

public class MVaadinServlet extends VaadinServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Log log = Log.getLog(MVaadinServlet.class);
	
	private ResourceNode<?> config;

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
	        	config = MConfig.createConfigFor(mhusConfigUrl.toURI());
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


	public ResourceNode<?> getConfig() {
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
