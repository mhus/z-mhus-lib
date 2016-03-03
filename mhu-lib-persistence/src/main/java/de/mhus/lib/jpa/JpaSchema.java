package de.mhus.lib.jpa;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.MObject;

/**
 * <p>Abstract JpaSchema class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class JpaSchema extends MObject {

	/**
	 * <p>getObjectTypes.</p>
	 *
	 * @return an array of {@link java.lang.Class} objects.
	 */
	public abstract Class<?>[] getObjectTypes();

	/**
	 * This should be called after the manager is created.
	 *
	 * @param manager a {@link de.mhus.lib.jpa.JpaManager} object.
	 */
	public void doPostInit(JpaManager manager) {
	}

	/**
	 * Overwrite this method to get the configuration object and initialize the schem. It should be
	 * called by the creator to initialize the schema before it is given to the manager.
	 *
	 * @param config a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 */
	public void doInit(ResourceNode config) {

	}

	/**
	 * <p>getSchemaName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSchemaName() {
		return getClass().getSimpleName();
	}

}
