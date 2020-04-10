package de.mhus.lib.core.config;

import java.util.List;

import de.mhus.lib.core.MProperties;

public class IConfig extends MProperties {

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
	
	public void setObject(String keyt, IConfig object) {
		
	}

	public List<String> getPropertyKeys() {
		return null;
	}

	public String getName() {
		return name;
	}

	public IConfig getParent() {
		return parent;
	}

}
