package de.mhus.lib.core.configupdater;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;

public class ConfigInt extends ConfigValue<Integer>{

	public ConfigInt(Object owner, String path, int def) {
		super(owner, path, def);
	}

	@Override
	protected Integer loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) 
			return MSingleton.getConfig(getOwner()).getInt(getPath(), getDefault());
		ResourceNode node = MSingleton.getConfig(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		return node.getInt(getPath().substring(p+1), getDefault());
	}

}
