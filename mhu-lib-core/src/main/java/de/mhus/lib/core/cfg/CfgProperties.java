package de.mhus.lib.core.cfg;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.util.PropertiesSubset;

public class CfgProperties extends CfgValue<IProperties> {

	public CfgProperties(Object owner, String path) {
		super(owner, path, new MProperties() );
	}

	@Override
	protected IProperties loadValue() {

		ResourceNode node = MSingleton.getCfg(getOwner(), null);
		if (node == null) return getDefault();
		if (MString.isEmpty(getPath()))
			return node;
		
		return new PropertiesSubset(node, getPath());
	}

	@Override
	protected IProperties loadValue(String value) {
		return MProperties.explodeToMProperties(value.split("\n"));
	}

}
