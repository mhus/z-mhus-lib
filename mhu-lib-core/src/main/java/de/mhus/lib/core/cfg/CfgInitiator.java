package de.mhus.lib.core.cfg;

import de.mhus.lib.core.system.CfgManager;
import de.mhus.lib.core.system.ISingletonInternal;

/**
 * <p>CfgInitiator interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public interface CfgInitiator {

	/**
	 * <p>doInitialize.</p>
	 *
	 * @param internal a {@link de.mhus.lib.core.system.ISingletonInternal} object.
	 * @param manager a {@link de.mhus.lib.core.system.CfgManager} object.
	 */
	void doInitialize(ISingletonInternal internal, CfgManager manager);
	
}
