/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
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

import java.util.Collections;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MPassword;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.aaa.Aaa;
import de.mhus.lib.core.aaa.BearerConfiguration;
import de.mhus.lib.core.aaa.TrustedToken;
import de.mhus.lib.core.cfg.CfgLong;
import de.mhus.lib.core.cfg.CfgNode;
import de.mhus.lib.core.node.INode;
import de.mhus.lib.core.util.SecureString;
import de.mhus.lib.core.util.SoftHashMap;
import de.mhus.lib.errors.NotFoundRuntimeException;

// TODO Use hashed passwords
public class TrustFromConfiguration extends MLog implements TrustApi {

    public enum TYPE {
        PLAIN,
        JWT
    };

    public static final BearerConfiguration BEARER_CONFIG = new BearerConfiguration();

    @SuppressWarnings("unused")
    private static final CfgLong CFG_BEARER_CONFIG_TIMEOUT =
            new CfgLong(TrustApi.class, "bearerTimeout", MPeriod.HOUR_IN_MILLISECONDS)
                    .updateAction(v -> BEARER_CONFIG.setTimeout(v))
                    .doUpdateAction();

    private Map<String, SecureString> passwordCache =
            Collections.synchronizedMap(new SoftHashMap<>());
    private Map<String, TYPE> typeCache = Collections.synchronizedMap(new SoftHashMap<>());
    private Map<String, String> targetCache = Collections.synchronizedMap(new SoftHashMap<>());

    private CfgNode config =
            new CfgNode(TrustApi.class, "", null)
                    .updateAction(
                            (c) -> {
                                synchronized (passwordCache) {
                                    passwordCache.clear();
                                }
                            });

    public SecureString getPassword(String name) {
        if (MString.isSet(name)) {
            SecureString ret = passwordCache.get(name);
            if (ret == null) {
                INode node = config.value();
                if (node != null) {
                    for (INode trust : node.getObjects()) {
                        if (trust.getString("name", "").equals(name)) {
                            ret = MPassword.decodeSecure(trust.getString("password", ""));
                            passwordCache.put(name, ret);
                        }
                    }
                }
            }
            if (ret != null) return ret;
        }
        throw new NotFoundRuntimeException("unknown trust", name);
    }

    public TYPE getType(String name) {
        TYPE ret = typeCache.get(name);
        if (ret == null) {
            INode node = config.value();
            if (node != null) {
                for (INode trust : node.getObjects()) {
                    if (trust.getString("name", "").equals(name)) {
                        String str = trust.getString("type", "JWT");
                        ret = TYPE.valueOf(str);
                        typeCache.put(name, ret);
                    }
                }
            }
        }
        if (ret != null) return ret;
        throw new NotFoundRuntimeException("unknown trust", name);
    }

    @Override
    public AuthenticationToken createToken(String ticket) {
        String[] parts = ticket.split(":", 3);
        validatePassword(parts[0], parts[2]);
        return new TrustedToken(parts[1]);
    }

    public boolean validatePassword(String name, String password) {
        SecureString trustPassword = getPassword(name);
        return password.equals(trustPassword.value());
    }

    @Override
    public String createToken(String source, Object target, Subject subject) {
        String trust = getTrustFor(source, target);
        switch (getType(trust)) {
            case PLAIN:
                return createTrustTicket(trust, getPassword(trust), subject);
            case JWT:
                String tokenStr =
                        Aaa.createBearerToken(subject, MSystem.getHostname(), BEARER_CONFIG);
                return Aaa.TICKET_PREFIX_BEARER + ":" + tokenStr;
        }
        throw new NotFoundRuntimeException("unknown trust type", trust);
    }

    public String getTrustFor(String source, Object target) {
        String name = source + ":" + target;
        String ret = targetCache.get(name);
        if (ret == null) {
            INode node = config.value();
            if (node != null) {
                for (INode trust : node.getObjects()) {
                    if (trust.getString("source", "").matches(source)) {
                        ret = trust.getString("name", null);
                        targetCache.put(name, ret);
                        break;
                    }
                }
                if (ret == null)
                    throw new NotFoundRuntimeException("trust not found for source", source);
            }
        }
        return ret;
    }
}
