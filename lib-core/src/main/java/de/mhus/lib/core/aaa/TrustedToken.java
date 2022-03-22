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
package de.mhus.lib.core.aaa;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;

import de.mhus.lib.core.M;
import de.mhus.lib.core.M.DEBUG;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.cache.CacheConfig;
import de.mhus.lib.core.cache.ICache;
import de.mhus.lib.core.cache.ICacheService;
import de.mhus.lib.core.cfg.CfgBoolean;
import de.mhus.lib.core.cfg.CfgLong;
import de.mhus.lib.core.logging.Log;

/**
 * To use TrustedToken you need to give access to one of the calling classes in the stacktrace to
 * access the user or all users. Give access to
 *
 * <p>de.mhus.lib.core.aaa.TrustedToken:admin:de.mhus.karaf.commands.impl.CmdAccessAdmin
 *
 * <p>to let de.mhus.karaf.commands.impl.CmdAccessAdmin access 'admin' or
 *
 * <p>de.mhus.lib.core.aaa.TrustedToken:*:de.mhus.karaf.commands.impl.CmdAccessLogin
 *
 * <p>to let de.mhus.karaf.commands.impl.CmdAccessLogin access all users (this is default).
 *
 * @author mikehummel
 */
public class TrustedToken implements AuthenticationToken {

    private static final Log log = Log.getLog(TrustedToken.class);
    private static final long serialVersionUID = 1L;
    private String principal;
    private static final CfgBoolean useCache =
            new CfgBoolean(AccessApi.class, "callerCacheEnabled", true);
    private static final CfgLong cacheTTL =
            new CfgLong(AccessApi.class, "callerCacheTTL", MPeriod.HOUR_IN_MILLISECONDS);
    private static ICache<String, Boolean> callerCache;

    public TrustedToken(String principal) {
        this.principal = principal;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    public final boolean hasAccess(DEBUG debugPermissions) {
        return hasAccess(debugPermissions, principal);
    }

    private static boolean hasAccess(DEBUG debugPermissions, String username) {

        // you can sudo to your own
        if (username.equals(Aaa.getPrincipal())) return true;

        // create caller ident for cache
        StringBuilder sb = new StringBuilder();
        sb.append(Aaa.getPrincipalOrGuest()).append(':');
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            String clazz = element.getClassName();
            //            for (String ignore : ignoreClassList)
            //                if (!clazz.startsWith(ignore)) {
            sb.append(clazz).append(' ');
            //                    break;
            //                }
        }
        if (sb.length() == 0) {
            if (debugPermissions != DEBUG.NO)
                log.i("TrustedToken access denied (1)", (Object) stackTrace);
            throw new AuthenticationException(
                    Aaa.getPrincipal() + ": TrustedToken access denied (1)");
        }

        String callerName = sb.toString().trim();
        if (useCache.value()) {
            initCache();
            if (callerCache != null) {
                Boolean cached = callerCache.get(callerName);
                if (cached != null) {
                    if (cached) {
                        if (debugPermissions != DEBUG.NO)
                            log.i(
                                    Aaa.getPrincipal() + ": TrustedToken access granted by cache",
                                    callerName);
                        if (debugPermissions == DEBUG.TRACE)
                            log.d(MSystem.currentStackTrace(callerName));
                        return true;
                    } else {
                        if (debugPermissions != DEBUG.NO)
                            log.i(
                                    Aaa.getPrincipal() + ": TrustedToken access denied (2)",
                                    callerName);
                        if (debugPermissions == DEBUG.TRACE)
                            log.d(MSystem.currentStackTrace(callerName));
                        return false;
                    }
                }
            }
        }

        // really check access
        boolean access = false;
        String prefix = TrustedToken.class.getCanonicalName() + ":" + username + ":";
        for (StackTraceElement element : stackTrace) {
            String clazz = element.getClassName();
            if (debugPermissions != DEBUG.NO) log.d("hasAccess", Aaa.getPrincipal(), prefix, clazz);
            if (Aaa.hasAccess(prefix + clazz)) {
                access = true;
                break;
            }
        }

        if (callerCache != null) callerCache.put(callerName, access);

        return access;
    }

    private static synchronized void initCache() {
        if (!useCache.value() || callerCache != null) return;

        try {
            ICacheService cacheService = M.l(ICacheService.class);
            if (cacheService == null) return;
            callerCache =
                    cacheService.createCache(
                            TrustedToken.class,
                            "caller",
                            String.class,
                            Boolean.class,
                            new CacheConfig().setHeapSize(10000).setTTL(cacheTTL.value()));
        } catch (Throwable t) {
            log.i(t);
        }
    }

    @Override
    public String toString() {
        return MSystem.toString(this, principal);
    }
}
