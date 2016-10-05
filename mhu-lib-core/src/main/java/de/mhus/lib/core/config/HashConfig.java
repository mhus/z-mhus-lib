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

public class HashConfig extends IConfig {

	private HashMap<String, String> 	properties 	= null;
	private HashMap<String, LinkedList<HashConfig>> children	= null;
	private LinkedList<HashConfig> childrenAll	= null;
	private String name;
	private WritableResourceNode parent;
	
	public HashConfig() {
		this(null,null);
	}
	
	public HashConfig(String name,WritableResourceNode parent) {
		this.name = name;
		this.parent = parent;
		properties = new HashMap<String, String>();
		children = new HashMap<String, LinkedList<HashConfig>>();
		childrenAll = new LinkedList<HashConfig>();
	}

	public HashConfig(Map<String, String> fill) {
		this(null,null);
		if (fill !=null) properties.putAll(fill);
	}

	@Override
	public String[] getPropertyKeys() {
		return properties.keySet().toArray(new String[properties.size()]);
	}

	@Override
	public ResourceNode getNode(String key) {
		LinkedList<HashConfig> list = children.get(key);
		if (list == null || list.size() == 0) return null;
		return list.getFirst();
	}

	@Override
	public ResourceNode[] getNodes(String key) {
		LinkedList<HashConfig> list = children.get(key);
		if (list == null || list.size() == 0) return new ResourceNode[0];
		return list.toArray(new HashConfig[list.size()]);
	}

	@Override
	public ResourceNode[] getNodes() {
		return childrenAll.toArray(new HashConfig[childrenAll.size()]);
	}
	
	@Override
	public String[] getNodeKeys() {
		return children.keySet().toArray(new String[children.size()]);
	}

	@Override
	public String getName() {
		return name;
	}

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

	public void unlink() throws MException {
		if (parent == null) return;
		parent.removeConfig(this);
		parent = null;
	}
	
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

	@Override
	public String getProperty(String name) {
		return properties.get(name);
	}

	@Override
	public boolean isProperty(String name) {
		return properties.containsKey(name);
	}

	@Override
	public void removeProperty(String key) {
		properties.remove(key);
	}

	@Override
	public void setProperty(String key, Object value) {
		properties.put(key, MCast.objectToString(value));
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public ResourceNode getParent() {
		return parent;
	}

	@Override
	public Set<String> keys() {
		return properties.keySet();
	}

	@Override
	public InputStream getInputStream(String key) {
		return null;
	}

	@Override
	public URL getUrl() {
		return null;
	}

}
