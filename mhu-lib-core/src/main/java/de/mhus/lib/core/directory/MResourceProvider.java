package de.mhus.lib.core.directory;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.lang.MObject;

/**
 * <p>Abstract MResourceProvider class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@DefaultImplementation(ClassLoaderResourceProvider.class)
public abstract class MResourceProvider<T extends ResourceNode> extends MObject {

	/**
	 * <p>getResource.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a T object.
	 */
	public abstract T getResource(String name);
	
}
