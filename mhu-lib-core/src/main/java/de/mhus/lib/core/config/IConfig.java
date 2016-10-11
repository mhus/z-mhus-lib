package de.mhus.lib.core.config;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.directory.WritableResourceNode;

@DefaultImplementation(DefaultConfigFile.class)
public abstract class IConfig extends WritableResourceNode {

	@Override
	public boolean isValid() {
		return true;
	}
	
	@Override
	public boolean hasContent() {
		return true;
	}
	
}
