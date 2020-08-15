package de.mhus.lib.test.util;

import de.mhus.lib.core.cfg.CfgInitiator;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.mapi.IApiInternal;
import de.mhus.lib.core.mapi.MCfgManager;
import de.mhus.lib.test.MConfigTest;

public class SampleInitiator implements CfgInitiator {

    @Override
    public void doInitialize(IApiInternal internal, MCfgManager manager, IConfig config) {
        MConfigTest.initiate(config);
    }
}
