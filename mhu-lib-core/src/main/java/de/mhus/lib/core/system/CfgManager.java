package de.mhus.lib.core.system;

import java.util.HashMap;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.cfg.CfgProvider;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.MObject;

public class CfgManager extends MLog {

	private HashMap<String, CfgProvider> configurations = new HashMap<>();
	private CentralMhusCfgProvider provider;
	
	public CfgManager() {
		//defaultConfig = new HashConfig();
	}
	
	public CfgManager(CentralMhusCfgProvider provider) {
		this.provider = provider;
	}
	
	public void registerCfgProvider(String name, CfgProvider provider) {
		if (name == null) return;
		if (provider == null) {
			CfgProvider old = configurations.remove(name);
			if (old != null) old.doStop();
		} else {
			configurations.put(name, provider);
			provider.doStart();
		}
	}
	
	public ResourceNode getCfg(Object owner, ResourceNode def) {
		initCfg();
		
		Class<?> c = null;
		if (owner instanceof String) {
			String name = (String)owner;
			ResourceNode cClass = getCfg(name);
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
			ResourceNode cClass = getCfg(name);
			if (cClass != null) {
				log().t("found (2)",owner.getClass(),name);
				return cClass;
			}
			c = c.getSuperclass();
		}
		log().t("not found",owner.getClass());			
	
		return def;
	}
	
	private void initCfg() {
		
	}

	public ResourceNode getCfg(String owner) {
		initCfg();
		
		CfgProvider p = configurations.get(owner);
		if (p != null) {
			ResourceNode cOwner = p.getConfig();
			if (cOwner != null) return cOwner;
		}
		IConfig defaultConfig = provider.getConfig();
		if (defaultConfig == null) return null;
		ResourceNode  cOwner = defaultConfig.getNode(owner);
		return cOwner;
	}
	
	public ResourceNode getCfg(String owner, ResourceNode def) {
		initCfg();

		ResourceNode cClass = getCfg(owner);
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
			ResourceNode cClass = getCfg(name);
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
			ResourceNode cClass = getCfg(name);
			if (cClass != null) {
//				log().t("found (2)",owner.getClass(),name);
				return name.equals(n);
			}
			c = c.getSuperclass();
		}
		
		return false;
	}

	public void reConfigure() {
		provider.reConfigure();
	}


}
