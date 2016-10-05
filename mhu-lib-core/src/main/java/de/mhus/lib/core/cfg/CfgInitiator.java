package de.mhus.lib.core.cfg;

import de.mhus.lib.core.system.CfgManager;
import de.mhus.lib.core.system.ISingletonInternal;

public interface CfgInitiator {

	void doInitialize(ISingletonInternal internal, CfgManager manager);
	
}
