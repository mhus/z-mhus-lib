package de.mhus.lib.jpa;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.MObject;

public abstract class JpaSchema extends MObject {

	public abstract Class<?>[] getObjectTypes();

	/**
	 * This should be called after the manager is created.
	 * 
	 * @param manager
	 */
	public void doPostInit(JpaManager manager) {
	}
	
	/**
	 * Overwrite this method to get the configuration object and initialize the schem. It should be
	 * called by the creator to initialize the schema before it is given to the manager.
	 * 
	 * @param config
	 */
	public void doInit(ResourceNode config) {
		
	}

	public String getSchemaName() {
		return getClass().getSimpleName();
	}

}
