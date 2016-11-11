package de.mhus.lib.core.config;

import java.util.Collection;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.directory.WritableResourceNode;

@DefaultImplementation(DefaultConfigFile.class)
public abstract class IConfig extends WritableResourceNode<IConfig> {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean isValid() {
		return true;
	}
	
	@Override
	public boolean hasContent() {
		return false;
	}
	
	@Override
	public Collection<String> getRenditions() {
		return null;
	}
	
	@Override
	public IProperties getRenditionProperties(String rendition) {
		return null;
	}


}
