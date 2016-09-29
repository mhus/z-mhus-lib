package de.mhus.lib.core.config;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.directory.WritableResourceNode;

/**
 * <p>Abstract IConfig class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@DefaultImplementation(DefaultConfigFile.class)
public abstract class IConfig extends WritableResourceNode {

	/** {@inheritDoc} */
	@Override
	public boolean isValide() {
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean hasContent() {
		return true;
	}
	
}
