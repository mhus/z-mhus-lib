package de.mhus.lib.core.cfg;

import de.mhus.lib.core.system.CfgManager;
import de.mhus.lib.core.system.IApiInternal;

public interface CfgInitiator {

	void doInitialize(IApiInternal internal, CfgManager manager);
	
}
