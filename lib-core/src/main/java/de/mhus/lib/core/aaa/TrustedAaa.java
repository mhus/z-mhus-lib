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
        Collection<Realm> realms =
                ((DefaultSecurityManager) SecurityUtils.getSecurityManager()).getRealms();
        for (Realm realm : realms) {
            try {
                AuthenticationInfo info =
                        realm.getAuthenticationInfo(
                                new TrustedToken(String.valueOf(subject.getPrincipal())));
                if (info != null && info instanceof SimpleAccount) {
                    Collection<Permission> perms = ((SimpleAccount) info).getObjectPermissions();
                    if (perms == null) return (Collection<String>) M.EMPTY_LIST;
                    ArrayList<String> out = new ArrayList<>(perms.size());
                    for (Permission perm : perms) out.add(perm.toString());
                    return out;
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return null;
    }
}
