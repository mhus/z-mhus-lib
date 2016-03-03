package de.mhus.lib.core.config;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.directory.WritableResourceNode;
import de.mhus.lib.errors.MException;

/**
 * <p>HashConfig class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class HashConfig extends IConfig {

	private HashMap<String, String> 	properties 	= null;
	private HashMap<String, LinkedList<HashConfig>> children	= null;
	private LinkedList<HashConfig> childrenAll	= null;
	private String name;
	private WritableResourceNode parent;
	
	/**
	 * <p>Constructor for HashConfig.</p>
	 */
	public HashConfig() {
		this(null,null);
	}
	
	/**
	 * <p>Constructor for HashConfig.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param parent a {@link de.mhus.lib.core.directory.WritableResourceNode} object.
	 */
	public HashConfig(String name,WritableResourceNode parent) {
		this.name = name;
		this.parent = parent;
		properties = new HashMap<String, String>();
		children = new HashMap<String, LinkedList<HashConfig>>();
		childrenAll = new LinkedList<HashConfig>();
	}

	/**
	 * <p>Constructor for HashConfig.</p>
	 *
	 * @param fill a {@link java.util.Map} object.
	 */
	public HashConfig(Map<String, String> fill) {
		this(null,null);
		if (fill !=null) properties.putAll(fill);
	}

	/** {@inheritDoc} */
	@Override
	public String[] getPropertyKeys() {
		return properties.keySet().toArray(new String[properties.size()]);
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode getNode(String key) {
		LinkedList<HashConfig> list = children.get(key);
		if (list == null || list.size() == 0) return null;
		return list.getFirst();
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode[] getNodes(String key) {
		LinkedList<HashConfig> list = children.get(key);
		if (list == null || list.size() == 0) return new ResourceNode[0];
		return list.toArray(new HashConfig[list.size()]);
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode[] getNodes() {
		return childrenAll.toArray(new HashConfig[childrenAll.size()]);
	}
	
	/** {@inheritDoc} */
	@Override
	public String[] getNodeKeys() {
		return children.keySet().toArray(new String[children.size()]);
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return name;
	}

	/** {@inheritDoc} */
	@Override
	public WritableResourceNode createConfig(String key) throws MException {
		HashConfig child = new HashConfig(key,this);
		LinkedList<HashConfig> list = children.get(key);
		if (list == null) {
			list = new LinkedList<HashConfig>();
			children.put(key, list);
		}
		list.add(child);
		childrenAll.add(child);
		return child;
	}

	/**
	 * <p>unlink.</p>
	 *
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void unlink() throws MException {
		if (parent == null) return;
		parent.removeConfig(this);
		parent = null;
	}
	
	/**
	 * <p>setConfig.</p>
	 *
	 * @param key a {@link java.lang.String} object.
	 * @param child a {@link de.mhus.lib.core.config.HashConfig} object.
	 * @return a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public ResourceNode setConfig(String key, HashConfig child) throws MException {
		if (child.parent != null) throw new MException("Config already linked");
		LinkedList<HashConfig> list = children.get(key);
		if (list == null) {
			list = new LinkedList<HashConfig>();
			children.put(key, list);
		}
		child.parent = this;
		child.name = key;
		list.add(child);
		childrenAll.add(child);
		return child;
	}
	
	// TODO move in childAll list
	/** {@inheritDoc} */
	@Override
	public int moveConfig(ResourceNode config, int newPos) throws MException {

		if (!(config instanceof HashConfig))
			throw new MException("not a hashconfig");

		LinkedList<HashConfig> list = children.get(config.getName());
		if (list == null || list.size() == 0 || !list.contains(config))
			throw new MException("config not in my list");
		
		if (list.size() == 1) {
			if (newPos == MOVE_FIRST || newPos == MOVE_LAST || newPos == 0)
				return 0;
			throw new MException("out of range");
		}
		
		if (newPos == MOVE_FIRST) {
			list.remove(config);
			list.add(0, (HashConfig) config);
			return 0;
		}
		
		if(newPos == MOVE_LAST) {
			list.remove(config);
			list.add((HashConfig) config);
			return list.size()-1;
		}
		
		if (newPos == MOVE_DOWN) {
			int pos = list.indexOf(config);
			if (pos == list.size()-1)
				throw new MException("out of range");				
			list.remove(config);
			list.add(pos+1,(HashConfig) config);
			return pos+1;
		}
		
		if (newPos == MOVE_UP) {
			int pos = list.indexOf(config);
			if (pos == 0)
				throw new MException("out of range");				
			list.remove(config);
			list.add(pos-1,(HashConfig) config);
			return pos-1;
		}
		
		if (newPos < 0 || newPos >= list.size())
			throw new MException("out of range");			

		list.remove(config);
		list.add(newPos,(HashConfig) config);
		
		return newPos;
	}

	/** {@inheritDoc} */
	@Override
	public void removeConfig(ResourceNode config) throws MException {

		if (!(config instanceof HashConfig)) return;
			
		LinkedList<HashConfig> list = children.get(config.getName());
		if (list == null) return;
		list.remove(config);
		if (list.size() == 0)
			children.remove(list);

		childrenAll.remove(config);

	}

	/** {@inheritDoc} */
	@Override
	public String getProperty(String name) {
		return properties.get(name);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isProperty(String name) {
		return properties.containsKey(name);
	}

	/** {@inheritDoc} */
	@Override
	public void removeProperty(String key) {
		properties.remove(key);
	}

	/** {@inheritDoc} */
	@Override
	public void setProperty(String key, Object value) {
		properties.put(key, MCast.objectToString(value));
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEditable() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode getParent() {
		return parent;
	}

	/** {@inheritDoc} */
	@Override
	public Set<String> keys() {
		return properties.keySet();
	}

	/** {@inheritDoc} */
	@Override
	public InputStream getInputStream(String key) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public URL getUrl() {
		return null;
	}

}
