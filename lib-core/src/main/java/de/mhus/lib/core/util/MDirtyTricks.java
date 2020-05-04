package de.mhus.lib.core.util;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.cfg.CfgValue;

// This class is for use with JUnit tests or similar
public class MDirtyTricks {

    public static boolean updateCfgValue(Class<?> owner, String path, String value) {
        for (CfgValue<?> cfg : MApi.getCfgUpdater().getList()) {
            if (cfg.getOwner().equals(owner.getCanonicalName().toLowerCase())
                    && cfg.getPath().equals(path)) {
                cfg.setValue(value);
                return true;
            }
        }
        return false;
    }
}
