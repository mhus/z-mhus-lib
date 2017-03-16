package de.mhus.lib.core.cfg;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.directory.ResourceNode;

public class CfgInt extends CfgValue<Integer>{

	public CfgInt(Object owner, String path, int def) {
		super(owner, path, def);
	}

	@Override
	protected Integer loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) 
			return MApi.getCfg(getOwner()).getInt(getPath(), getDefault());
		ResourceNode node = MApi.getCfg(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		return node.getInt(getPath().substring(p+1), getDefault());
	}

	@Override
	protected Integer loadValue(String value) {
		return MCast.toint(value, 0);
	}

}
