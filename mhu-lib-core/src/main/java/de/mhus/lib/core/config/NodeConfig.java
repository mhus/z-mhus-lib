package de.mhus.lib.core.config;

import java.util.HashMap;
import java.util.Properties;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.directory.WritableResourceNode;
import de.mhus.lib.errors.MException;

/**
 * <p>NodeConfig class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class NodeConfig extends PropertiesConfig {
	
	private HashMap<String, ResourceNode> configurations = new HashMap<String, ResourceNode>();
	
	/**
	 * <p>Constructor for NodeConfig.</p>
	 */
	public NodeConfig() {
		super(new Properties());
	}
	
	/**
	 * <p>setConfig.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param config a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 */
	public void setConfig(String name, ResourceNode config) {
		configurations.put(name, config);
	}
	
	/** {@inheritDoc} */
	@Override
	public ResourceNode getNode(String key) {
		return configurations.get(key);
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode[] getNodes(String key) {
		return new ResourceNode[] { getNode(key)};
//		return configurations.values().toArray(new IConfig[configurations.size()]);
	}

	/**
	 * <p>getConfigKeys.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getConfigKeys() {
		return configurations.keySet().toArray(new String[configurations.size()]);
	}

	/** {@inheritDoc} */
	@Override
	public WritableResourceNode createConfig(String key) throws MException {
		NodeConfig newConfig = new NodeConfig();
		setConfig(key, newConfig);
		return newConfig;
	}

}
