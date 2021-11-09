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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleRole;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import de.mhus.lib.core.M;
import de.mhus.lib.core.M.DEBUG;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.cache.CacheConfig;
import de.mhus.lib.core.cache.ICache;
import de.mhus.lib.core.cache.ICacheService;
import de.mhus.lib.core.cfg.CfgBoolean;
import de.mhus.lib.core.cfg.CfgLong;

/**
 * Load user and role definitions from txt or xml files.
 *
 * <p>XML User Format:
 *
 * <p><user password=""> <roles> <role>role name</role> </roles> <perms> <perm>Permission</perm>
 * </perms> <data> <key>value</key> </data> </user>
 *
 * <p>XML Role Format:
 *
 * <p><role> <perms> <perm>Permission</perm> </perms> </role>
 *
 * @author mikehummel
 */
public abstract class FileSourceRealm extends AbstractRealm implements PrincipalDataRealm {

    protected String resourcesPath;
    protected File userDir;
    protected File rolesDir;
    protected String defaultRole;
    protected String rolePermission;
    private ICache<String, SimpleAccount> userCacheApi;
    private ICache<String, SimpleRole> roleCacheApi;

    @SuppressWarnings("rawtypes")
    private ICache<String, HashMap> dataCacheApi;

    private boolean useCache;
    private long cacheTTL = MPeriod.HOUR_IN_MILLISECOUNDS;

    @SuppressWarnings("unused")
    private CfgBoolean CFG_USE_CACHE =
            new CfgBoolean(getClass(), "cacheEnabled", true)
                    .updateAction(v -> setUseCache(v))
                    .doUpdateAction();

    @SuppressWarnings("unused")
    private CfgLong CFG_CACHE_TTL =
            new CfgLong(getClass(), "cacheTTL", MPeriod.MINUTE_IN_MILLISECOUNDS * 30)
                    .updateAction(v -> setCacheTTL(v))
                    .doUpdateAction();

    public FileSourceRealm() {
        setCredentialsMatcher(new CombiCredentialsMatcher());
    }

    @Override
    protected void onInit() {
        userDir = new File(resourcesPath + File.separator + "users");
        rolesDir = new File(resourcesPath + File.separator + "roles");

        super.onInit();
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = getUsername(principals);
        return getUser(username);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(String username, AuthenticationToken token)
            throws AuthenticationException {

        SimpleAccount account = getUser(username);

        if (account != null) {

            if (account.isLocked()) {
                throw new LockedAccountException("Account [" + account + "] is locked.");
            }
            if (account.isCredentialsExpired()) {
                String msg = "The credentials for account [" + account + "] are expired";
                throw new ExpiredCredentialsException(msg);
            }
            return account;
        }
        return null;
    }

    protected SimpleAccount getUser(String username) {

        if (Aaa.USER_ADMIN.value().equals(username)) return Aaa.ACCOUNT_ADMIN;
        if (Aaa.USER_GUEST.value().equals(username)) return Aaa.ACCOUNT_GUEST;

        if (useCache) {
            initCache();
            if (userCacheApi != null) {
                SimpleAccount cached = userCacheApi.get(username);
                if (cached != null) return cached;
            }
        }
        
        try {
            SimpleAccount account = createUser(username);
            if (account != null) {
                if (useCache && userCacheApi != null) userCacheApi.put(username, account);
            } else
                log.d("user not found", username);
            return account;
        } catch (Exception e) {
            log.d(username, e);
            return null;
        }
    }

    protected abstract SimpleAccount createUser(String username) throws Exception;

    private synchronized void initCache() {
        if (!useCache || userCacheApi != null) return;
        try {
            ICacheService cacheService = M.l(ICacheService.class);
            if (cacheService == null) return;
            userCacheApi =
                    cacheService.createCache(
                            this,
                            getName() + ":user",
                            String.class,
                            SimpleAccount.class,
                            new CacheConfig().setHeapSize(10000).setTTL(cacheTTL));
            roleCacheApi =
                    cacheService.createCache(
                            this,
                            getName() + ":role",
                            String.class,
                            SimpleRole.class,
                            new CacheConfig().setHeapSize(10000).setTTL(cacheTTL));
            dataCacheApi =
                    cacheService.createCache(
                            this,
                            getName() + ":data",
                            String.class,
                            HashMap.class,
                            new CacheConfig().setHeapSize(10000).setTTL(cacheTTL));
        } catch (Throwable t) {
            log.d(t);
        }
    }

    public SimpleRole getRole(String rolename) {

        if (useCache) {
            initCache();
            if (roleCacheApi != null) {
                SimpleRole cached = roleCacheApi.get(rolename);
                if (cached != null) return cached;
            }
        }

        try {
            SimpleRole role = createRole(rolename);

            if (role != null) {
                if (useCache && roleCacheApi != null) roleCacheApi.put(rolename, role);
            } else
                log.d("role not found", rolename);
            return role;
        } catch (Exception e) {
            log.d(rolename, e);
            return null;
        }
    }

    protected abstract SimpleRole createRole(String rolename) throws Exception;

    public String getResourcesPath() {
        return resourcesPath;
    }

    public void setResourcesPath(String resourcesPath) {
        this.resourcesPath = resourcesPath;
    }

    @Override
    public Map<String, String> getUserData(Subject subject) {
        String username = Aaa.getPrincipal(subject);

        if (useCache) {
            initCache();
            if (dataCacheApi != null) {
                @SuppressWarnings("unchecked")
                Map<String, String> cached = dataCacheApi.get(username);
                if (cached != null) return cached;
            }
        }

        try {
            HashMap<String, String> data = createData(username);

            if (data != null) {
                if (useCache && dataCacheApi != null) dataCacheApi.put(username, data);
            } else
                log.d("data not found", username);
            return data;
        } catch (Exception e) {
            log.d(username, e);
            return null;
        }
    }

    protected abstract HashMap<String, String> createData(String username) throws Exception;

    public String getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }

    @Override
    protected boolean isPermitted(Permission permission, AuthorizationInfo info) {
        boolean ret = super.isPermitted(permission, info);
        if (debugPermissions != DEBUG.NO && !ret) {
            log.d("perm access denied", Aaa.CURRENT_PRINCIPAL_OR_GUEST, permission);
            if (debugPermissions == DEBUG.TRACE)
                log.d(MSystem.currentStackTrace(String.valueOf(permission)));
        }
        return ret;
    }

    @Override
    protected boolean hasRole(String roleIdentifier, AuthorizationInfo info) {
        // 1. check role to permission mapping
        if (rolePermission != null
                && isPermitted(
                        new WildcardPermission(rolePermission + ":*:" + roleIdentifier), info))
            return true;
        // 2. check default role access
        boolean ret = super.hasRole(roleIdentifier, info);
        if (debugPermissions != DEBUG.NO && !ret) {
            if (!Aaa.ROLE_ADMIN.value().equals(roleIdentifier))
                log.d("role access denied", Aaa.CURRENT_PRINCIPAL_OR_GUEST, roleIdentifier);
            if (debugPermissions == DEBUG.TRACE) log.d(MSystem.currentStackTrace(roleIdentifier));
        }

        return ret;
    }

    public String getRolePermission() {
        return rolePermission;
    }

    /**
     * This option maps role access to permissions. Role access check is also active as next check.
     *
     * <p>Set the role permission to not null to enable general role permission check. If you set it
     * to "admin" and give "admin:*" permission and a user have the permission then the user has
     * access to all roles. Use "admin:*:user" to permit acess to the role "user".
     *
     * @param rolePermission
     */
    public void setRolePermission(String rolePermission) {
        this.rolePermission = rolePermission;
    }

    @Override
    public String createBearerToken(
            Subject subject, String issuer, BearerConfiguration configuration)
            throws ShiroException {
        String username = Aaa.getPrincipal(subject);
        SimpleAccount user = getUser(username);
        if (user != null && !user.isLocked())
            return M.l(JwtProvider.class).createBearerToken(username, issuer, configuration);
        throw new UnknownAccountException("User unknown: " + username);
    }

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

    public void invalidateUserCache(String username) {
        if (useCache) {
            if (dataCacheApi != null) {
                dataCacheApi.remove(username);
            }
            if (userCacheApi != null) {
                userCacheApi.remove(username);
            }
        }

    }

    public void invalidateRoleCache(String rolename) {
        if (useCache) {
            if (roleCacheApi != null) {
                roleCacheApi.remove(rolename);
            }
        }

    }

}
