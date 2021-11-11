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
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleRole;
import org.apache.shiro.util.PermissionUtils;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;

public class TxtFileSourceRealm extends FileSourceRealm {

    @Override
    protected SimpleAccount createUser(String username) throws Exception {
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

            return account;
        }
        return null;
    }

    @Override
    protected SimpleRole createRole(String rolename) throws Exception {
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

            return role;
        }
        return null;
    }

    @Override
    protected HashMap<String, String> createData(String username) throws Exception {
        // load from FS
        File file = new File(userDir, MFile.normalize(username) + ".properties");
        if (file.exists() && file.isFile()) {
            log.d("load data", username, "properties");
            MProperties prop = MProperties.load(file);
            HashMap<String, String> ret = new HashMap<>();
            for (Entry<String, Object> entry : prop.entrySet())
                ret.put(entry.getKey(), String.valueOf(entry.getValue()));

            return ret;
        }
        return null;
    }
}
