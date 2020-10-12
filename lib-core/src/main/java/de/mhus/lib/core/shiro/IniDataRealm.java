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
package de.mhus.lib.core.shiro;

import java.util.HashMap;
import java.util.Map;

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
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.config.Ini;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;

import de.mhus.lib.core.M;
import de.mhus.lib.core.logging.Log;

public class IniDataRealm extends IniRealm implements PrincipalDataRealm, BearerRealm {

    public static final String DATA_SECTION_NAME = "data";
    private static final transient Log log = Log.getLog(IniDataRealm.class);

    private HashMap<String, Map<String, String>> userData = new HashMap<>();
    private boolean debugPermissions;
    private String rolePermission;

    public IniDataRealm() {
        super();
        setCredentialsMatcher(new CombiCredentialsMatcher() );
    }

    public IniDataRealm(Ini ini) {
        super(ini);
        setCredentialsMatcher(new CombiCredentialsMatcher() );
    }

    public IniDataRealm(String resourcePath) {
        super(resourcePath);
        setCredentialsMatcher(new CombiCredentialsMatcher() );
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token != null && BearerToken.class.isAssignableFrom(token.getClass()))
            return true;
        return super.supports(token);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        
        if (token instanceof UsernamePasswordToken)
            return super.doGetAuthenticationInfo(token);
        
        if (token instanceof BearerToken) {
            String tokenStr = ((BearerToken)token).getToken();
            JwsData jwtToken = M.l(JwtProvider.class).readToken(tokenStr);
            String username = jwtToken.getSubject();
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
        }

//      throw new UnknownAccountException(username);
      return null;
    }

    @Override
    public Map<String, String> getUserData(Subject subject) {
        String userName = AccessUtil.getPrincipal(subject);
        Map<String, String> data = userData.get(userName);
        if (data == null) return null;
        return data;
    }

    @Override
    protected void onInit() {
        super.onInit();
        Ini ini = getIni();

        processDefinitions(ini);
    }

    private void processDefinitions(Ini ini) {
        if (CollectionUtils.isEmpty(ini)) {
            log.w("defined, but the ini instance is null or empty.", getClass().getSimpleName());
            return;
        }

        Ini.Section dataSection = ini.getSection(DATA_SECTION_NAME);
        if (!CollectionUtils.isEmpty(dataSection)) {
            log.d("Discovered the section.  Processing...", DATA_SECTION_NAME);
            processDataDefinitions(dataSection);
        }
    }

    protected void processDataDefinitions(Map<String, String> dataDefs) {
        if (dataDefs == null || dataDefs.isEmpty()) {
            return;
        }
        for (String key : dataDefs.keySet()) {
            String value = dataDefs.get(key);

            int pos = key.indexOf('#');
            if (pos > 0) {
                String userName = key.substring(0, pos);
                String subKey = key.substring(pos + 1);

                Map<String, String> data = userData.get(userName);
                if (data == null) {
                    data = new HashMap<>();
                    userData.put(userName, data);
                }
                data.put(subKey, value);
            }
        }
    }

    @Override
    protected boolean isPermitted(Permission permission, AuthorizationInfo info) {
        boolean ret = super.isPermitted(permission, info);
        if (debugPermissions && !ret) {
            log.d("perm access denied", AccessUtil.CURRENT_PRINCIPAL, permission);
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
            log.d("role access denied", AccessUtil.CURRENT_PRINCIPAL, roleIdentifier);
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
    public String createBearerToken(Subject subject, String issuer, BearerConfiguration configuration) throws ShiroException {
        String userName = AccessUtil.getPrincipal(subject);
        SimpleAccount user = getUser(userName);
        if (user != null)
            return M.l(JwtProvider.class).createBearerToken(userName, issuer, configuration);
        throw new UnknownAccountException("User unknown: " + userName);
    }
}
