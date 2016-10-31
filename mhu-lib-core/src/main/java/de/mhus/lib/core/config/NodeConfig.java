package de.mhus.lib.core.config;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import de.mhus.lib.errors.MException;

public class NodeConfig extends PropertiesConfig {
	
	private static final long serialVersionUID = 1L;
	private HashMap<String, IConfig> configurations = new HashMap<>();
	
	public NodeConfig() {
		super(new Properties());
	}
	
	public void setConfig(String name, IConfig config) {
		configurations.put(name, config);
	}
	
	@Override
	public IConfig getNode(String key) {
		return configurations.get(key);
	}

	@Override
	public List<IConfig> getNodes(String key) {
		LinkedList<IConfig> out = new LinkedList<>();
		out.add(getNode(key));
		return out;
//		return configurations.values().toArray(new IConfig[configurations.size()]);
	}

	public String[] getConfigKeys() {
		return configurations.keySet().toArray(new String[configurations.size()]);
	}

	@Override
	public IConfig createConfig(String key) throws MException {
		NodeConfig newConfig = new NodeConfig();
		setConfig(key, newConfig);
		return newConfig;
	}

}
