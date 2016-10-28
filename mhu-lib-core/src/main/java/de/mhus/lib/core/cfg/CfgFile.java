package de.mhus.lib.core.cfg;

import java.io.File;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

public class CfgFile extends CfgValue<File>{

	public CfgFile(Object owner, String path, File def) {
		super(owner, path, def);
	}

	@Override
	protected File loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) {
			String str = null;
			try {
				str = MSingleton.getCfg(getOwner()).getExtracted(getPath(), null);
			} catch (MException e) {}
			if (str == null) return getDefault();
			return new File(str);
		}
		ResourceNode node = MSingleton.getCfg(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		String str = null;
		try {
			str = node.getExtracted(getPath().substring(p+1), null);
		} catch (MException e) {}
		if (str == null) return getDefault();
		return new File(str);
	}

}
