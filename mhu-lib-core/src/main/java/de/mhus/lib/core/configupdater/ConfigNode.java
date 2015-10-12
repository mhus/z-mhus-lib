package de.mhus.lib.core.configupdater;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.directory.ResourceNode;

public class ConfigNode extends ConfigValue<IConfig>{

	public ConfigNode(Object owner, String path, IConfig def) {
		super(owner, path, def);
	}

	@Override
	protected IConfig loadValue() {
		ResourceNode node = MSingleton.getConfig(getOwner()).getNodeByPath(getPath());
		if (node == null) return getDefault();
		return (IConfig) node;
	}

}
