package de.mhus.lib.core;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.lang.IBase;
import de.mhus.lib.core.system.DefaultHousekeeper;

/**
 * <p>Abstract MHousekeeper interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@DefaultImplementation(DefaultHousekeeper.class)
public abstract interface MHousekeeper extends IBase {
		
	/**
	 * <p>register.</p>
	 *
	 * @param task a {@link de.mhus.lib.core.MHousekeeperTask} object.
	 * @param sleep a long.
	 * @param weak a boolean.
	 */
	void register(MHousekeeperTask task, long sleep, boolean weak);

}
