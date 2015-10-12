package de.mhus.lib.core.configupdater;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;

public class ConfigBoolean extends ConfigValue<Boolean>{

	public ConfigBoolean(Object owner, String path, boolean def) {
		super(owner, path, def);
	}

	@Override
	protected Boolean loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) 
			return MSingleton.getConfig(getOwner()).getBoolean(getPath(), getDefault());
		ResourceNode node = MSingleton.getConfig(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		return node.getBoolean(getPath().substring(p+1), getDefault());
	}

}
