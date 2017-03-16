package de.mhus.lib.core.cfg;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.directory.ResourceNode;

public class CfgLong extends CfgValue<Long>{

	public CfgLong(Object owner, String path, long def) {
		super(owner, path, def);
	}

	@Override
	protected Long loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) 
			return MApi.getCfg(getOwner()).getLong(getPath(), getDefault());
		ResourceNode node = MApi.getCfg(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		return node.getLong(getPath().substring(p+1), getDefault());
	}

	@Override
	protected Long loadValue(String value) {
		return MCast.tolong(value, 0);
	}

}
