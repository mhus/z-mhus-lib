/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.security;

import java.util.List;

import de.mhus.lib.basics.Ace;
import de.mhus.lib.core.M;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.security.Account;
import de.mhus.lib.core.security.Rightful;

public class AaaUtil {

    private static Log log = Log.getLog(AaaUtil.class);

    public static boolean hasAccess(AaaContext context, List<String> acl) {
        if (context.isAdminMode()) return true;
        return hasAccess(context.getAccount(), acl);
    }

    public static boolean hasAccess(Rightful accessControl, List<String> acl) {
        if (accessControl == null || acl == null) return false;

        try {
            for (String line : acl) {
                if (line == null) continue;
                if (line.startsWith(" ") || line.startsWith("=")) continue;
                line = line.trim();
                if (line.length() == 0 || line.startsWith(AccessApi.ACCESS_COMMENT)) continue;
                if (line.startsWith(AccessApi.ACCESS_NOT)) {
                    line = line.substring(4);
                    if (accessControl.hasGroup(line)) return false;
                } else if (line.startsWith(AccessApi.ACCESS_NOT_USER)) {
                    line = line.substring(8);
                    if (accessControl.getName().equals(line)) return false;
                } else if (line.startsWith(AccessApi.ACCESS_USER)) {
                    line = line.substring(5);
                    if (accessControl.getName().equals(line)) return true;
                } else if (line.equals(AccessApi.ACCESS_ALL) || accessControl.hasGroup(line))
                    return true;
            }
        } catch (Throwable t) {
            log.d(acl, accessControl, t);
        }
        return false;
    }

    public static boolean hasAccess(Rightful accessControl, String[] acl) {
        if (accessControl == null || acl == null) return false;

        try {
            for (String line : acl) {
                if (line == null) continue;
                if (line.startsWith(" ") || line.startsWith("=")) continue;
                line = line.trim();
                if (line.length() == 0 || line.startsWith(AccessApi.ACCESS_COMMENT)) continue;
                if (line.startsWith(AccessApi.ACCESS_NOT)) {
                    line = line.substring(4);
                    if (accessControl.hasGroup(line)) return false;
                } else if (line.startsWith(AccessApi.ACCESS_NOT_USER)) {
                    line = line.substring(8);
                    if (accessControl.getName().equals(line)) return false;
                } else if (line.startsWith(AccessApi.ACCESS_USER)) {
                    line = line.substring(5);
                    if (accessControl.getName().equals(line)) return true;
                } else if (line.equals(AccessApi.ACCESS_ALL) || accessControl.hasGroup(line))
                    return true;
            }
        } catch (Throwable t) {
            log.d(acl, accessControl, t);
        }
        return false;
    }

    public static boolean hasAccess(Account account, String acl) {
        if (acl == null) return false;
        String[] parts = acl.split(AccessApi.ACCESS_SEPARATOR);
        return hasAccess(account, parts);
    }

    public static Ace getAccessAce(AaaContext context, List<String> acl) {
        if (context.isAdminMode()) return Ace.ACE_ALL;
        return getAccessAce(context.getAccount(), acl);
    }

    public static Ace getAccessAce(Rightful accessControl, List<String> acl) {
        if (accessControl == null || acl == null) return Ace.ACE_NONE;

        try {
            for (String line : acl) {
                if (line == null) continue;
                if (line.startsWith(" ") || line.startsWith("=")) continue;
                line = line.trim();
                if (line.length() == 0 || line.startsWith(AccessApi.ACCESS_COMMENT)) continue;
                int p = line.indexOf(AccessApi.ACCESS_IS);
                String rule = line;
                String ace = Ace.RIGHTS_NONE;
                if (p >= 0) {
                    rule = line.substring(0, p);
                    ace = line.substring(p + 1);
                }
                rule = rule.trim();
                ace = ace.trim();
                if (rule.startsWith(AccessApi.ACCESS_USER)) {
                    rule = rule.substring(5);
                    if (accessControl.getName().equals(rule)) return new Ace(ace);
                } else if (rule.equals(AccessApi.ACCESS_ALL) || accessControl.hasGroup(rule))
                    return new Ace(ace);
            }
        } catch (Throwable t) {
            log.d(acl, accessControl, t);
        }
        return Ace.ACE_NONE;
    }

    public static Ace getAccessAce(AaaContext context, String acl) {
        if (context.isAdminMode()) return Ace.ACE_ALL;
        return getAccessAce(context.getAccount(), acl.split(AccessApi.ACCESS_SEPARATOR));
    }

    public static Ace getAccessAce(AaaContext context, String[] acl) {
        if (context.isAdminMode()) return Ace.ACE_ALL;
        return getAccessAce(context.getAccount(), acl);
    }

    public static Ace getAccessAce(Rightful accessControl, String[] acl) {
        if (accessControl == null || acl == null) return Ace.ACE_NONE;

        try {
            for (String line : acl) {
                if (line == null) continue;
                if (line.startsWith(" ") || line.startsWith("=")) continue;
                line = line.trim();
                if (line.length() == 0 || line.startsWith(AccessApi.ACCESS_COMMENT)) continue;
                int p = line.indexOf(AccessApi.ACCESS_IS);
                String rule = line;
                String ace = Ace.RIGHTS_NONE;
                if (p >= 0) {
                    rule = line.substring(0, p);
                    ace = line.substring(p + 1);
                } else {
                    // allow single access definitions like "r" for all
                    rule = AccessApi.ACCESS_ALL;
                    ace = line;
                }
                rule = rule.trim();
                ace = ace.trim();
                if (rule.startsWith(AccessApi.ACCESS_USER)) {
                    rule = rule.substring(5);
                    if (accessControl.getName().equals(rule)) return new Ace(ace);
                } else if (rule.equals(AccessApi.ACCESS_ALL) || accessControl.hasGroup(rule))
                    return new Ace(ace);
            }
        } catch (Throwable t) {
            log.d(acl, accessControl, t);
        }
        return Ace.ACE_NONE;
    }

    public static AaaContext enterRoot() {
        AccessApi api = M.l(AccessApi.class);
        if (api == null) return null;
        return api.processAdminSession();
    }

    //	public static void leaveRoot() {
    //		AccessApi api = M.l(AccessApi.class);
    //		if (api == null) return;
    //		AaaContext context = api.getCurrent();
    //		if (context == null || !context.isAdminMode() ||
    // !context.getAccount().getName().equals("root") || !context.isAdminMode()) return;
    //		api.release(context);
    //	}

    public static Account currentAccount() {
        AccessApi api = M.l(AccessApi.class);
        if (api == null) return null;
        AaaContext context = api.getCurrent();
        return context.getAccount();
    }

    public static AaaContext currentContext() {
        AccessApi api = M.l(AccessApi.class);
        if (api == null) return null;
        AaaContext context = api.getCurrent();
        return context;
    }

    public static boolean isCurrentAdmin() {
        AccessApi api = M.l(AccessApi.class);
        if (api == null) return false;
        AaaContext context = api.getCurrent();
        if (context == null) return false;
        return context.isAdminMode();
    }

    public static boolean isGuest(Account account) {
        if (account == null) return true;
        return account.isSynthetic() && AccessApi.GUEST_NAME.equals(account.getName());
    }

    public static boolean isRoot(Account account) {
        if (account == null) return true;
        return account.isSynthetic() && AccessApi.ROOT_NAME.equals(account.getName());
    }
}
