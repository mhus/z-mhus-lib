/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
