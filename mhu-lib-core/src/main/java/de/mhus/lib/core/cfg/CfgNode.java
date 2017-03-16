package de.mhus.lib.core.cfg;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.directory.ResourceNode;

public class CfgNode extends CfgValue<IConfig>{

	public CfgNode(Object owner, String path, IConfig def) {
		super(owner, path, def);
	}

	@Override
	protected IConfig loadValue() {
		ResourceNode node = MApi.getCfg(getOwner()).getNodeByPath(getPath());
		if (node == null) return getDefault();
		return (IConfig) node;
	}

	@Override
	protected IConfig loadValue(String value) {
		return null;
	}

}
