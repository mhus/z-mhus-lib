package de.mhus.lib.core.cfg;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;

public class CfgString extends CfgValue<String>{

	public CfgString(Object owner, String path, String def) {
		super(owner, path, def);
	}

	@Override
	protected String loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) 
			return MSingleton.getCfg(getOwner()).getString(getPath(), getDefault());
		ResourceNode node = MSingleton.getCfg(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		return node.getString(getPath().substring(p+1), getDefault());
	}

}
