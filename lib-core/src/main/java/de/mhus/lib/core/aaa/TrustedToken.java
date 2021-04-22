package de.mhus.lib.core.aaa;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.cache.CacheConfig;
import de.mhus.lib.core.cache.ICache;
import de.mhus.lib.core.cache.ICacheService;
import de.mhus.lib.core.cfg.CfgBoolean;
import de.mhus.lib.core.cfg.CfgLong;
import de.mhus.lib.core.logging.Log;

/**
 * To use TrustedToken you need to give access to one of the calling classes in the stacktrace to access the
 * user or all users. Give access to
 * 
 * de.mhus.lib.core.aaa.TrustedToken:admin:de.mhus.karaf.commands.impl.CmdAccessAdmin
 * 
 * to let de.mhus.karaf.commands.impl.CmdAccessAdmin access 'admin'
 * or
 * 
 * de.mhus.lib.core.aaa.TrustedToken:*:de.mhus.karaf.commands.impl.CmdAccessLogin
 * 
 * to let de.mhus.karaf.commands.impl.CmdAccessLogin access all users (this is default).
 * 
 * 
 * @author mikehummel
 *
 */
public class TrustedToken implements AuthenticationToken {

    private static final Log log = Log.getLog(TrustedToken.class);
    private static final long serialVersionUID = 1L;
    private String principal;
    private static final CfgBoolean useCache = new CfgBoolean(AccessApi.class, "callerCacheEnabled", true);
    private static final CfgLong cacheTTL = new CfgLong(AccessApi.class, "callerCacheTTL", MPeriod.HOUR_IN_MILLISECOUNDS);
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

    public final boolean hasAccess(boolean debugPermissions) {
        return hasAccess(debugPermissions, principal);
    }

    
    private static boolean hasAccess(boolean debugPermissions, String username) {

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
            if (debugPermissions)
                log.i("TrustedToken access denied (1)",stackTrace);
            throw new AuthenticationException("TrustedToken access denied (1)");
        }

        String callerName = sb.toString().trim();
        if (useCache.value()) {
            initCache();
            if (callerCache != null) {
                Boolean cached = callerCache.get(callerName);
                if (cached != null) {
                    if (cached) {
                        if (debugPermissions)
                            log.i("TrustedToken access granted by cache",callerName);
                        return true;
                    } else {
                        if (debugPermissions)
                            log.i("TrustedToken access denied (2)",callerName);
                        return false;
                    }
                }
            }
        }
        
        // really check access
        boolean access = false;
        for (StackTraceElement element : stackTrace) {
            String clazz = element.getClassName();
            if (Aaa.hasAccess( TrustedToken.class.getCanonicalName() + ":" + username + ":" + clazz )) {
                access = true;
                break;
            }
        }
    
        if (callerCache != null)
            callerCache.put(callerName, access);

        return access;
    }

    private static synchronized void initCache() {
        if (!useCache.value() || callerCache != null) return;

        try {
            ICacheService cacheService = M.l(ICacheService.class);
            if (cacheService == null) return;
            callerCache = cacheService.createCache(
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
