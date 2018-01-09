package de.mhus.lib.core.cfg;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.directory.ResourceNode;

public class CfgBoolean extends CfgValue<Boolean>{

	public CfgBoolean(Object owner, String path, boolean def) {
		super(owner, path, def);
	}

	@Override
	protected Boolean loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) 
			return MApi.getCfg(getOwner()).getBoolean(getPath(), getDefault());
		ResourceNode<?> node = MApi.getCfg(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		return node.getBoolean(getPath().substring(p+1), getDefault());
	}

	@Override
	protected Boolean loadValue(String value) {
		return MCast.toboolean(value, false);
	}

}
