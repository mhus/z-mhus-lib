package de.mhus.lib.core.directory;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.lang.MObject;

@DefaultImplementation(ClassLoaderResourceProvider.class)
public abstract class MResourceProvider<T extends ResourceNode> extends MObject {

	public abstract T getResource(String name);
	
}
