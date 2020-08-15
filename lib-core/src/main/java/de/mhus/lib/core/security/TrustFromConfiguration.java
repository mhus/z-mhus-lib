package de.mhus.lib.core.security;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MPassword;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.cfg.CfgNode;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.util.SecureString;
import de.mhus.lib.core.util.SoftHashMap;
import de.mhus.lib.errors.NotFoundRuntimeException;

// TODO Use hashed passwords
public class TrustFromConfiguration extends MLog implements TrustApi {

    private SoftHashMap<String, SecureString> cache = new SoftHashMap<>();
    private CfgNode config =
            new CfgNode(TrustApi.class, "", null)
                    .updateAction(
                            (c) -> {
                                synchronized (cache) {
                                    cache.clear();
                                }
                            });

    @Override
    public SecureString getPassword(String name) {
        if (MString.isSet(name)) {
            synchronized (cache) {
                SecureString ret = cache.get(name);
                if (ret == null) {
                    IConfig node = config.value();
                    if (node != null) {
                        for (IConfig trust : node.getObjects()) {
                            if (trust.getString("name", "").equals(name)) {
                                ret = MPassword.decodeSecure(trust.getString("password", ""));
                                cache.put(name, ret);
                            }
                        }
                    }
                }
                if (ret != null) return ret;
            }
        }
        throw new NotFoundRuntimeException("unknown trust", name);
    }

    @Override
    public boolean validatePassword(String name, String password) {
        SecureString trustPassword = getPassword(name);
        return password.equals(trustPassword.value());
    }
}
