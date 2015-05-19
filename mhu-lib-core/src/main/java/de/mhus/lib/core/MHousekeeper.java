package de.mhus.lib.core;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.lang.IBase;
import de.mhus.lib.core.system.DefaultHousekeeper;

@DefaultImplementation(DefaultHousekeeper.class)
public abstract interface MHousekeeper extends IBase {
		
	void register(MHousekeeperTask task, long sleep, boolean weak);

}
