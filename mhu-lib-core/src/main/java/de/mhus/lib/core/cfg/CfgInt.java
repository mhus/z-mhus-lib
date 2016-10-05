package de.mhus.lib.core.cfg;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;

public class CfgInt extends CfgValue<Integer>{

	public CfgInt(Object owner, String path, int def) {
		super(owner, path, def);
	}

	@Override
	protected Integer loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) 
			return MSingleton.getCfg(getOwner()).getInt(getPath(), getDefault());
		ResourceNode node = MSingleton.getCfg(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		return node.getInt(getPath().substring(p+1), getDefault());
	}

}
