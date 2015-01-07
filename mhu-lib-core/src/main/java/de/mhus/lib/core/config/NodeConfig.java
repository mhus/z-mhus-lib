package de.mhus.lib.core.config;

import java.util.HashMap;
import java.util.Properties;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.directory.WritableResourceNode;
import de.mhus.lib.errors.MException;

public class NodeConfig extends PropertiesConfig {
	
	private HashMap<String, ResourceNode> configurations = new HashMap<String, ResourceNode>();
	
	public NodeConfig() {
		super(new Properties());
	}
	
	public void setConfig(String name, ResourceNode config) {
		configurations.put(name, config);
	}
	
	public ResourceNode getNode(String key) {
		return configurations.get(key);
	}

	public ResourceNode[] getNodes(String key) {
		return new ResourceNode[] { getNode(key)};
//		return configurations.values().toArray(new IConfig[configurations.size()]);
	}

	public String[] getConfigKeys() {
		return configurations.keySet().toArray(new String[configurations.size()]);
	}

	public WritableResourceNode createConfig(String key) throws MException {
		NodeConfig newConfig = new NodeConfig();
		setConfig(key, newConfig);
		return newConfig;
	}

}
