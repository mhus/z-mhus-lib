package de.mhus.lib.core.system;

import java.util.HashMap;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.MObject;

public class ConfigManager extends MLog {

	private HashMap<String, ConfigProvider> configurations = new HashMap<>();
	private ConfigProvider provider;
	
	public ConfigManager() {
		//defaultConfig = new HashConfig();
	}
	
	public ConfigManager(ConfigProvider provider) {
		this.provider = provider;
	}
	
	public void registerConfigProvider(String name, ConfigProvider provider) {
		if (name == null) return;
		if (provider == null)
			configurations.remove(name);
		else
			configurations.put(name, provider);
	}
	
	public ResourceNode getConfig(Object owner, ResourceNode def) {
		initConfig();
		
		Class<?> c = null;
		if (owner instanceof String) {
			String name = (String)owner;
			ResourceNode cClass = getConfig(name);
			if (cClass != null) {
				log().t("found (1)",name);
				return cClass;
			}
		} else
		if (owner instanceof Class) {
			c = (Class<?>) owner;
		} else {
			c = owner.getClass();
		}
		while (c != null) {
			String name = c.getCanonicalName();
			ResourceNode cClass = getConfig(name);
			if (cClass != null) {
				log().t("found (2)",owner.getClass(),name);
				return cClass;
			}
			c = c.getSuperclass();
		}
		log().t("not found",owner.getClass());			
	
		return def;
	}
	
	private void initConfig() {
		
	}

	public ResourceNode getConfig(String owner) {
		initConfig();
		
		ConfigProvider p = configurations.get(owner);
		if (p != null) {
			ResourceNode cOwner = p.getConfig();
			if (cOwner != null) return cOwner;
		}
		IConfig defaultConfig = provider.getConfig();
		if (defaultConfig == null) return null;
		ResourceNode  cOwner = defaultConfig.getNode(owner);
		return cOwner;
	}
	
	public ResourceNode getConfig(String owner, ResourceNode def) {
		initConfig();

		ResourceNode cClass = getConfig(owner);
		if (cClass != null) {
			log().t("found (3)",owner.getClass(),owner);
			return cClass;
		}

		return def;
	}

	public boolean isOwner(Object owner, String n) {

		Class<?> c = null;
		if (owner instanceof String) {
			String name = (String)owner;
			ResourceNode cClass = getConfig(name);
			if (cClass != null) {
//				log().t("found (1)",name);
				return name.equals(n);
			}
		} else
		if (owner instanceof Class) {
			c = (Class<?>) owner;
		} else {
			c = owner.getClass();
		}
		while (c != null) {
			String name = c.getCanonicalName();
			ResourceNode cClass = getConfig(name);
			if (cClass != null) {
//				log().t("found (2)",owner.getClass(),name);
				return name.equals(n);
			}
			c = c.getSuperclass();
		}
		
		return false;
	}

}
