package de.mhus.lib.core.aaa;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleRole;
import org.apache.shiro.util.PermissionUtils;
import org.w3c.dom.Element;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MPassword;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MXml;

public class XmlFileSourceRealm extends FileSourceRealm {

    @Override
    protected SimpleAccount createUser(String username) throws Exception {
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
                            Set<Permission> perm = role.getPermissions();
                            if (perm != null) account.addObjectPermissions(perm);
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

            return account;
        }
        return null;
    }

    @Override
    protected SimpleRole createRole(String rolename) throws Exception {
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

            return role;
        }
        return null;
    }

    @Override
    protected HashMap<String, String> createData(String username) throws Exception {
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

                return ret;
            }
        }
        return null;
    }

}
