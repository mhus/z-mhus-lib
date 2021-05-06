package de.mhus.lib.core.aaa;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;

import de.mhus.lib.core.M;

public class TrustedAaa {

    @SuppressWarnings("unchecked")
    static Collection<String> getPerms(Subject subject) {
        if (subject == null) return null;
        Collection<Realm> realms = ((DefaultSecurityManager)SecurityUtils.getSecurityManager()).getRealms();
        for (Realm realm : realms) {
            try {
                AuthenticationInfo info = realm.getAuthenticationInfo(new TrustedToken(String.valueOf(subject.getPrincipal())));
                if (info != null && info instanceof SimpleAccount) {
                    Collection<Permission> perms = ((SimpleAccount)info).getObjectPermissions();
                    if (perms == null) return (Collection<String>) M.EMPTY_LIST;
                    ArrayList<String> out = new ArrayList<>(perms.size());
                    for (Permission perm : perms)
                        out.add(perm.toString());
                    return out;
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return null;
    }

}
