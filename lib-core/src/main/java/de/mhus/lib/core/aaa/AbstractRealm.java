package de.mhus.lib.core.aaa;

import java.util.HashSet;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.cache.CacheConfig;
import de.mhus.lib.core.cache.ICache;
import de.mhus.lib.core.cache.ICacheService;
import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.core.logging.Log;

public abstract class AbstractRealm extends AuthorizingRealm implements BearerRealm {

//    protected static final String[] ignoreClassList = new String[] {
//            "java.lang.",
//            "de.mhus.core"
//    }; // configurable?

    protected final Log log = Log.getLog(getClass());

    protected boolean useCache;
    protected long cacheTTL = MPeriod.HOUR_IN_MILLISECOUNDS;
    protected boolean debugPermissions;

    protected SimpleAccount ADMIN;
    protected SimpleAccount GUEST;

    protected final CfgString PERMS_GUEST = new CfgString(AccessApi.class, "permsGuest", 
            "de.mhus.lib.core.aaa.TrustedToken:admin:de.mhus.karaf.commands.impl.CmdAccessAdmin;" +
            "de.mhus.lib.core.aaa.TrustedToken:*:de.mhus.karaf.commands.impl.CmdAccessLogin"
            ).updateAction(
            v -> {
                if (GUEST != null && v != null) {
                    HashSet<Permission> perms = new HashSet<>();
                    for (String r : v.split(";"))
                        if (MString.isSetTrim(r))
                            perms.add(new WildcardPermission(r.trim()));
                    GUEST.setObjectPermissions(perms);
                }
            });
    
    protected final CfgString ROLES_GUEST = new CfgString(AccessApi.class, "rolesGuest", "").updateAction(
            v -> {
                if (GUEST != null && v != null) {
                    HashSet<String> roles = new HashSet<>();
                    for (String r : v.split(";"))
                        if (MString.isSetTrim(r))
                            roles.add(r.trim());
                    GUEST.setRoles(roles);
                }
            });

    protected final CfgString ROLES_ADMIN = new CfgString(AccessApi.class, "rolesAdmin", "").updateAction(
            v -> {
                if (ADMIN != null && v != null) {
                    HashSet<String> roles = new HashSet<>();
                    roles.add(Aaa.ROLE_ADMIN.value());
                    for (String r : v.split(";"))
                        if (MString.isSetTrim(r))
                            roles.add(r.trim());
                    ADMIN.setRoles(roles);
                }
            });

    private ICache<String, Boolean> callerCache;

    @Override
    protected void onInit() {

        ADMIN = new SimpleAccount(Aaa.USER_ADMIN.value(),"secret","");
        ADMIN.addStringPermission("*");
        ROLES_ADMIN.doUpdateAction();

        GUEST = new SimpleAccount(Aaa.USER_GUEST.value(),"secret","");
        PERMS_GUEST.doUpdateAction();
        ROLES_GUEST.doUpdateAction();

        super.onInit();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token != null && 
                (
                        token instanceof TrustedToken
                        ||
                        token instanceof BearerToken
                    )) return true;
        return super.supports(token);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {

        String username = null;
        if (token instanceof UsernamePasswordToken) {
            UsernamePasswordToken upToken = (UsernamePasswordToken) token;
            username = upToken.getUsername();
        } else if (token instanceof BearerToken) {
            String tokenStr = ((BearerToken) token).getToken();
            JwsData jwtToken = M.l(JwtProvider.class).readToken(tokenStr);
            username = jwtToken.getSubject();
        } else if (token instanceof TrustedToken) {
            username = (String)((TrustedToken)token).getPrincipal();
            
            if (username.equals(Aaa.USER_GUEST.value())) return GUEST;
            // check permissions to use trusted token
            
            // create caller ident for cache
            StringBuilder sb = new StringBuilder();
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            for (StackTraceElement element : stackTrace) {
                String clazz = element.getClassName();
//                for (String ignore : ignoreClassList)
//                    if (!clazz.startsWith(ignore)) {
                        sb.append(clazz).append(' ');
//                        break;
//                    }
            }
            if (sb.length() == 0) {
                if (debugPermissions)
                    log.i("TrustedToken access denied (1)",stackTrace);
                throw new AuthenticationException("TrustedToken access denied (1)");
            }

            String callerName = sb.toString().trim();
            if (useCache) {
                initCache();
                if (callerCache != null) {
                    Boolean cached = callerCache.get(callerName);
                    if (cached != null) {
                        if (cached) {
                            if (debugPermissions)
                                log.i("TrustedToken access granted by cache",callerName);
                            return doGetAuthenticationInfo(username, token);
                        } else {
                            if (debugPermissions)
                                log.i("TrustedToken access denied (2)",callerName);
                            throw new AuthenticationException("TrustedToken access denied (2)");
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
            
            if (!access) {
                if (debugPermissions)
                    log.i("TrustedToken access denied (3)",callerName);
                throw new AuthenticationException("TrustedToken access denied (3)");
            }
            if (debugPermissions)
                log.i("TrustedToken access granted",Aaa.getPrincipalOrGuest(),username,callerName);
        }

        if (username == null)
            throw new AuthenticationException("User or Token not found");

        return doGetAuthenticationInfo(username, token);
    }

    private synchronized void initCache() {
        if (!useCache || callerCache != null) return;

        try {
            ICacheService cacheService = M.l(ICacheService.class);
            if (cacheService == null) return;
            callerCache = cacheService.createCache(
                    this, 
                    getName() + ":caller", 
                    String.class, 
                    Boolean.class, 
                    new CacheConfig().setHeapSize(10000).setTTL(cacheTTL));
        } catch (Throwable t) {
            log.i(t);
        }

    }

    protected abstract AuthenticationInfo doGetAuthenticationInfo(String username, AuthenticationToken token);

    public boolean isUseCache() {
        return useCache;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    public long getCacheTTL() {
        return cacheTTL;
    }

    public void setCacheTTL(long cacheTTL) {
        this.cacheTTL = cacheTTL;
    }

    public boolean isDebugPermissions() {
        return debugPermissions;
    }

    public void setDebugPermissions(boolean debugPermissions) {
        this.debugPermissions = debugPermissions;
    }

}
