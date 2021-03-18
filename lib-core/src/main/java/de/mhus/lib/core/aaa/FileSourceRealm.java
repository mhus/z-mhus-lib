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
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleRole;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.PermissionUtils;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MPassword;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MXml;
import de.mhus.lib.core.cache.CacheConfig;
import de.mhus.lib.core.cache.ICache;
import de.mhus.lib.core.cache.ICacheService;
import de.mhus.lib.core.logging.Log;

/**
 * Load user and role definitions from txt or xml files.
 * 
 * XML User Format:
 * 
 * <user password="">
 *   <roles>
 *     <role>role name</role>
 *   </roles>
 *   <perms>
 *     <perm>Permission</perm>
 *   </perms>
 *   <data>
 *     <key>value</key>
 *   </data>
 * </user>
 * 
 * 
 * XML Role Format:
 * 
 * <role>
 *   <perms>
 *     <perm>Permission</perm>
 *   </perms>
 * </role>
 * 
 * @author mikehummel
 *
 */
public class FileSourceRealm extends AuthorizingRealm implements PrincipalDataRealm, BearerRealm {

    private static Log log = Log.getLog(FileSourceRealm.class);
    protected String resourcesPath;
    protected File userDir;
    protected File rolesDir;
    protected String defaultRole;
    protected boolean debugPermissions;
    protected String rolePermission;
    private boolean useCache;
    private long cacheTTL = MPeriod.HOUR_IN_MILLISECOUNDS;
    private ICache<String, SimpleAccount> userCacheApi;
    private ICache<String, SimpleRole> roleCacheApi;
    @SuppressWarnings("rawtypes")
    private ICache<String, Map> dataCacheApi;
    protected SimpleAccount ADMIN;

    public FileSourceRealm() {
        setCredentialsMatcher(new CombiCredentialsMatcher());
    }

    @Override
    protected void onInit() {
        userDir = new File(resourcesPath + File.separator + "users");
        rolesDir = new File(resourcesPath + File.separator + "roles");

        ADMIN = new SimpleAccount(Aaa.USER_ADMIN.value(),"secret","");
        ADMIN.addRole(Aaa.ROLE_ADMIN.value());
        ADMIN.addStringPermission("*");

        super.onInit();
        // setCachingEnabled(false); // true
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = getUsername(principals);
        return getUser(username);
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
        }

        if (username != null) {
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
        }

        //        throw new UnknownAccountException(username);
        return null;
    }

    protected String getUsername(PrincipalCollection principals) {
        return getAvailablePrincipal(principals).toString();
    }

    protected SimpleAccount getUser(String username) {
        
        if (Aaa.USER_ADMIN.value().equals(username)) return ADMIN;
        
        if (useCache) {
            initCache();
            if (userCacheApi != null) {
                SimpleAccount cached = userCacheApi.get(username);
                if (cached != null) return cached;
            }
        }
        
        try {
            // load from FS
            File file = new File(userDir, MFile.normalize(username) + ".txt");
            if (file.exists() && file.isFile()) {
                log.d("load user", username, "txt");
                List<String> lines = MFile.readLines(file, false);
                String password = lines.get(0);
                SimpleAccount account = new SimpleAccount(username, password, getName());
                account.setCredentials(password);
                lines.remove(0);
                for (String line : lines) {
                    line = line.trim();
                    if (MString.isEmpty(line) || line.startsWith("#")) continue;
                    String rolename = line;
                    SimpleRole role = this.getRole(rolename);
                    if (role != null) {
                        account.addRole(rolename);
                        account.addObjectPermissions(role.getPermissions());
                    }
                }
                if (MString.isSet(defaultRole)) {
                    SimpleRole role = this.getRole(defaultRole);
                    if (role != null) {
                        account.addRole(defaultRole);
                        account.addObjectPermissions(role.getPermissions());
                    }
                }
                
                if (useCache && userCacheApi != null)
                    userCacheApi.put(username, account);
                
                return account;
            }
        } catch (IOException e) {
            log.d(username, e);
            return null;
        }

        try {
            // load from FS
            File file = new File(userDir, MFile.normalize(username) + ".xml");
            if (file.exists() && file.isFile()) {
                log.d("load user", username, "xml");
                Element xml = MXml.loadXml(file).getDocumentElement();
                String password = MPassword.decode(xml.getAttribute("password"));
                SimpleAccount account = new SimpleAccount(username, password, getName());
                account.setCredentials(password);
                Element rolesE = MXml.getElementByPath(xml, "roles");
                if (rolesE != null) {
                    for (Element roleE : MXml.getLocalElementIterator(rolesE, "role")) {
                        String rolename = MXml.getValue(roleE, false);
                        if (MString.isSet(rolename)) {
                            SimpleRole role = getRole(rolename);
                            if (role != null) {
                                account.addRole(rolename);
                                account.addObjectPermissions(role.getPermissions());
                            }
                        }
                    }
                }

                Element permsE = MXml.getElementByPath(xml, "perms");
                if (permsE != null) {
                    HashSet<String> perms = new HashSet<>();
                    for (Element permE : MXml.getLocalElementIterator(permsE, "perm")) {
                        String perm = MXml.getValue(permE, false);
                        if (MString.isSet(perm)) perms.add(perm);
                    }
                    Set<Permission> permissions =
                            PermissionUtils.resolvePermissions(perms, getPermissionResolver());
                    account.addObjectPermissions(permissions);
                }
                
                if (useCache && userCacheApi != null)
                    userCacheApi.put(username, account);

                return account;
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.d(username, e);
            return null;
        }

        log.d("user not found", username);
        return null;
    }

    protected synchronized void initCache() {
        if (userCacheApi != null) return;
        try {
            ICacheService cacheService = M.l(ICacheService.class);
            if (cacheService == null) return;
            userCacheApi = cacheService.createCache(
                    this, 
                    getClass().getCanonicalName() + ":" + getName() + ":user", 
                    String.class, 
                    SimpleAccount.class, 
                    new CacheConfig().setHeapSize(10000).setTTL(cacheTTL));
            roleCacheApi = cacheService.createCache(
                    this, 
                    getClass().getCanonicalName() + ":" + getName() + ":role", 
                    String.class, 
                    SimpleRole.class, 
                    new CacheConfig().setHeapSize(10000).setTTL(cacheTTL));
            dataCacheApi = cacheService.createCache(
                    this, 
                    getClass().getCanonicalName() + ":" + getName() + ":data", 
                    String.class, 
                    Map.class, 
                    new CacheConfig().setHeapSize(10000).setTTL(cacheTTL));
        } catch (Throwable t) {
            
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
            // load from FS
            File file = new File(rolesDir, MFile.normalize(rolename) + ".txt");
            if (file.exists() && file.isFile()) {
                log.d("load role", rolename, "txt");
                SimpleRole role = new SimpleRole(rolename);
                List<String> lines = MFile.readLines(file, false);
                HashSet<String> perms = new HashSet<>();
                for (String line : lines) {
                    line = line.trim();
                    if (MString.isEmpty(line) || line.startsWith("#")) continue;
                    perms.add(line);
                }
                Set<Permission> permissions =
                        PermissionUtils.resolvePermissions(perms, getPermissionResolver());
                role.setPermissions(permissions);
                
                if (useCache && roleCacheApi != null)
                    roleCacheApi.put(rolename, role);
                
                return role;
            }
        } catch (IOException e) {
            log.d(rolename, e);
            return null;
        }

        try {
            // load from FS
            File file = new File(rolesDir, MFile.normalize(rolename) + ".xml");
            if (file.exists() && file.isFile()) {
                log.d("load role", rolename, "xml");
                Element xml = MXml.loadXml(file).getDocumentElement();
                SimpleRole role = new SimpleRole(rolename);
                Element permsE = MXml.getElementByPath(xml, "perms");
                if (permsE != null) {
                    HashSet<String> perms = new HashSet<>();
                    for (Element permE : MXml.getLocalElementIterator(permsE, "perm")) {
                        String perm = MXml.getValue(permE, false);
                        if (MString.isSet(perm)) perms.add(perm);
                    }
                    Set<Permission> permissions =
                            PermissionUtils.resolvePermissions(perms, getPermissionResolver());
                    role.setPermissions(permissions);
                }
                
                if (useCache && roleCacheApi != null)
                    roleCacheApi.put(rolename, role);

                return role;
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.d(rolename, e);
            return null;
        }
        log.d("role not found", rolename);
        return null;
    }

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
            // load from FS
            File file = new File(userDir, MFile.normalize(username) + ".properties");
            if (file.exists() && file.isFile()) {
                log.d("load data", username, "properties");
                MProperties prop = MProperties.load(file);
                HashMap<String, String> ret = new HashMap<>();
                for (Entry<String, Object> entry : prop.entrySet())
                    ret.put(entry.getKey(), String.valueOf(entry.getValue()));
                
                if (useCache && dataCacheApi != null)
                    dataCacheApi.put(username, ret);
                
                return ret;
            }
        } catch (Exception e) {
            log.d(username, e);
            return null;
        }

        try {
            // load from FS
            File file = new File(userDir, MFile.normalize(username) + ".xml");
            if (file.exists() && file.isFile()) {
                log.d("load data", username, "xml");
                Element xml = MXml.loadXml(file).getDocumentElement();
                Element dataE = MXml.getElementByPath(xml, "data");
                if (dataE != null) {
                    HashMap<String, String> ret = new HashMap<>();
                    for (Element datE : MXml.getLocalElementIterator(dataE)) {
                        String value = MXml.getValue(datE, false);
                        ret.put(datE.getNodeName(), value);
                    }
                    
                    if (useCache && dataCacheApi != null)
                        dataCacheApi.put(username, ret);

                    return ret;
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.d(username, e);
            return null;
        }

        return null;
    }

    public String getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }

    @Override
    protected boolean isPermitted(Permission permission, AuthorizationInfo info) {
        boolean ret = super.isPermitted(permission, info);
        if (debugPermissions && !ret) {
            log.d("perm access denied", Aaa.CURRENT_PRINCIPAL, permission);
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
        if (debugPermissions && !ret) {
            log.d("role access denied", Aaa.CURRENT_PRINCIPAL, roleIdentifier);
        }

        return ret;
    }

    public boolean isDebugPermissions() {
        return debugPermissions;
    }

    public void setDebugPermissions(boolean debugPermissions) {
        this.debugPermissions = debugPermissions;
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
        File file1 = new File(userDir, MFile.normalize(username) + ".properties");
        File file2 = new File(userDir, MFile.normalize(username) + ".xml");
        if (file1.exists() && file1.isFile() || file2.exists() && file2.isFile())
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
}
