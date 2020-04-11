package de.mhus.lib.core.config;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.errors.MException;

public class IConfig extends MProperties {

	public static final String VALUE = "_value";
	protected String name;
	protected IConfig parent;

	public IConfig() {
		
	}
	
	public IConfig(String name, IConfig parent) {
        this.name = name;
        this.parent = parent;
	}

	public boolean isObject(String key) {
		return false;
	}
	
	public IConfig getObject(String key) {
		return null;
	}
	
	public boolean isArray(String key) {
		return false;
	}
	
	public List<IConfig> getArray(String key) {
		return null;
	}

	public IConfig getNodeByPath(String path) {
		return null;
	}
	
	public String getExtracted(String key, String def) {
		return null;
	}

	public String getExtracted(String key) {
		return null;
	}

	public List<IConfig> getObjects() {
		return null;
	}
	
	public void setObject(String key, IConfig object) {
		// TODO Warn if already bound ? - clone?
		object.parent = this;
		object.name = key;
		put(key, object);
	}
	
	@SuppressWarnings({ "unchecked" })
	public void addObject(String key, IConfig object) {
		Object obj = get(key);
		if (obj != null) {
			if (obj instanceof List) {
				// ignore
			} else
			if (obj instanceof IConfig) {
				LinkedList<Object> list = new LinkedList<>();
				list.add(obj);
				obj = list;
				put(key, obj);
			} else {
				// overwrite non object and arrays
				obj = new LinkedList<>();
				put(key, obj);
			}
		} else {
			setObject(key, object);
			return;
		}
		// TODO Warn if already bound ? - clone?
		object.parent = this;
		object.name = key;
		((List<Object>)obj).add(object);
	}

	public IConfig createObject(String key) {
		IConfig obj = new IConfig();
		addObject(key, obj);
		return obj;
	}

	/**
	 * Return only the property keys without objects and arrays.
	 * 
	 * @return
	 */
	public List<String> getPropertyKeys() {
		return null;
	}

	public String getName() {
		return name;
	}

	public IConfig getParent() {
		return parent;
	}

    public static IConfig createFromResource(Class<?> owner, String fileName) throws MException {
    	return null;
    }

    public static IConfig createFromResource(File file) throws MException {
    	return null;
    }
    
	public List<String> getObjectKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	public String dump() {
		// TODO Auto-generated method stub
		return null;
	}
	
    public static void merge(IConfig from, IConfig to) throws MException {
    	// TODO
        for (IConfig node : from.getObjects()) {
            IConfig n = to.createObject(node.getName());
            for (String name : node.getPropertyKeys()) {
                n.put(name, node.get(name));
            }
            merge(node, (IConfig) n);
        }
    }

	public static IConfig createFromXml(Element documentElement) {
		// TODO Auto-generated method stub
		return null;
	}

	public static IConfig createFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
