package de.mhus.lib.core.directory;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.lang.MObject;

@DefaultImplementation(ClassLoaderResourceProvider.class)
public abstract class MResourceProvider<T extends ResourceNode> extends MObject {

	/**
	 * Return a requested resource.
	 * 
	 * @param path The name or path to the resource.
	 * @return The resource or null if not found.
	 */
	public abstract T getResource(String path);
	
}
