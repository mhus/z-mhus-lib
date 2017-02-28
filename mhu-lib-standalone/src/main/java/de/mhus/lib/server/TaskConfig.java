package de.mhus.lib.server;


import java.util.HashMap;
import java.util.Map;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.jmx.MJmx;

public class TaskConfig extends MJmx {

	private IConfig config;
	private Map<String, String> options = new HashMap<String, String>();
	private Map<String,Object> objects = new HashMap<String, Object>();
	
	public void init() throws Exception {
		init( MSingleton.lookup(IConfig.class));
	}

	public void init(IConfig config) throws Exception {
		
//		if (config == null)
//			config = MSingleton.instance().getConfig();
    	this.config = config;
				
	}
	
	public ResourceNode config() {
		return config;
	}

	public void setOptions(Map<String, String> options) {
		this.options = options;
	}

	public Map<String,Object> objects() {
		return objects;
	}

	public Map<String, String> getOptions() {
		return options;
	}
	
}
