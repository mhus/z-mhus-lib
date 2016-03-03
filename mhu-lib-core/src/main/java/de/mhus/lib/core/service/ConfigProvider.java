package de.mhus.lib.core.service;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.MObject;

/**
 * <p>ConfigProvider class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ConfigProvider extends MLog {

	private IConfig config;
	
	/**
	 * <p>Constructor for ConfigProvider.</p>
	 */
	public ConfigProvider() {
		config = new HashConfig();
	}
	
	/**
	 * <p>Constructor for ConfigProvider.</p>
	 *
	 * @param config a {@link de.mhus.lib.core.config.IConfig} object.
	 */
	public ConfigProvider(IConfig config) {
		this.config = config;
	}
	
	/**
	 * <p>Getter for the field <code>config</code>.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param def a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 * @return a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 */
	public ResourceNode getConfig(Object owner, ResourceNode def) {
		if (config == null) {
			if (owner instanceof MObject) {
				config = MSingleton.get().getBaseControl().getBaseOf(((MObject)owner)).lookup(IConfig.class);
			}
		}
		if (config != null) {
			Class<?> c = null;
			if (owner instanceof String) {
				String name = (String)owner;
				ResourceNode cClass = config.getNode(name);
				if (cClass != null) {
					log().t("found",name);
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
				ResourceNode cClass = config.getNode(name);
				if (cClass != null) {
					log().t("found",owner.getClass(),name);
					return cClass;
				}
				c = c.getSuperclass();
			}
			log().t("not found",owner.getClass());
		}
		return def;
	}

}
