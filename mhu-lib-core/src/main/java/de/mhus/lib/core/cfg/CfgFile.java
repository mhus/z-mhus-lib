package de.mhus.lib.core.cfg;

import java.io.File;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.directory.ResourceNode;

public class CfgFile extends CfgValue<File>{

	public CfgFile(Object owner, String path, File def) {
		super(owner, path, def);
	}

	@Override
	protected File loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) {
			String str = MApi.getCfg(getOwner()).getExtracted(getPath(), null);
			if (str == null) return getDefault();
			return new File(str);
		}
		ResourceNode<?> node = MApi.getCfg(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		String str = node.getExtracted(getPath().substring(p+1), null);
		if (str == null) return getDefault();
		return new File(str);
	}

	@Override
	protected File loadValue(String value) {
		return new File(value);
	}

}
