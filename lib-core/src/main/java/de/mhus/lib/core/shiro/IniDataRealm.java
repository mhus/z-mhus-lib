package de.mhus.lib.core.shiro;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.config.Ini;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;

import de.mhus.lib.core.logging.Log;

public class IniDataRealm extends IniRealm implements PrincipalDataRealm {

    public static final String DATA_SECTION_NAME = "data";
    private static transient final Log log = Log.getLog(IniDataRealm.class);

    private HashMap<String,Map<String,String>> userData = new HashMap<>();
    private boolean debugPermissions;
    private String rolePermission;
    
    public IniDataRealm() {
        super();
    }

    public IniDataRealm(Ini ini) {
        super(ini);
    }

    public IniDataRealm(String resourcePath) {
        super(resourcePath);
    }

    @Override
    public Map<String,String> getUserData(Subject subject) {
        String userName = AccessUtil.getPrincipal(subject);
        Map<String,String> data = userData.get(userName);
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
                String userName = key.substring(0,pos);
                String subKey = key.substring(pos+1);
                
                Map<String,String> data = userData.get(userName);
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
            log.d("perm access denied",AccessUtil.CURRENT_PRINCIPAL, permission);
        }
        return ret;
    }

    @Override
    protected boolean hasRole(String roleIdentifier, AuthorizationInfo info) {
        //1. check role to permission mapping
        if (rolePermission != null && isPermitted(new WildcardPermission(rolePermission + ":*:" + roleIdentifier), info))
            return true;
        //2. check default role access
        boolean ret = super.hasRole(roleIdentifier, info);
        if (debugPermissions && !ret) {
            log.d("role access denied",AccessUtil.CURRENT_PRINCIPAL, roleIdentifier);
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
     * Set the role permission to not null to enable general role permission check.
     * If you set it to "admin" and give "admin:*" permission and a user have the permission then the user has access to all
     * roles. Use "admin:*:user" to permit acess to the role "user".
     * 
     * @param rolePermission
     */
    public void setRolePermission(String rolePermission) {
        this.rolePermission = rolePermission;
    }
    
}
