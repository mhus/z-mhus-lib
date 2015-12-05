package de.mhus.lib.core.configupdater;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.util.PropertiesSubset;

public class ConfigProperties extends ConfigValue<IProperties> {

	public ConfigProperties(Object owner, String path) {
		super(owner, path, new MProperties() );
	}

	@Override
	protected IProperties loadValue() {

		ResourceNode node = MSingleton.getConfig(getOwner(), null);
		if (node == null) return getDefault();
		if (MString.isEmpty(getPath()))
			return node;
		
		return new PropertiesSubset(node, getPath());
	}

}
