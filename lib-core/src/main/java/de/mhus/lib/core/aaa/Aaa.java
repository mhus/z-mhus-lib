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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.authz.aop.AuthenticatedAnnotationHandler;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.apache.shiro.authz.aop.GuestAnnotationHandler;
import org.apache.shiro.authz.aop.PermissionAnnotationHandler;
import org.apache.shiro.authz.aop.RoleAnnotationHandler;
import org.apache.shiro.authz.aop.UserAnnotationHandler;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

import de.mhus.lib.annotations.generic.Public;
import de.mhus.lib.core.M;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MCollection;
import de.mhus.lib.core.MPassword;
import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.security.TrustApi;
import de.mhus.lib.core.util.SecureString;
import de.mhus.lib.core.util.Value;

public class Aaa {

    public static final CfgString USER_ADMIN = new CfgString(AccessApi.class, "adminUser", "admin");
    public static final CfgString USER_GUEST = new CfgString(AccessApi.class, "guestUser", "guest");
    public static final CfgString REALM_TRUST = new CfgString(AccessApi.class, "realmTrust", "trust");

    private static final Log log = Log.getLog(Aaa.class);
    public static final CfgString ROLE_ADMIN =
            new CfgString(AccessApi.class, "adminRole", "GLOBAL_ADMIN");
    private static final String ATTR_LOCALE = "locale";
    private static final String TICKET_PREFIX_TRUST = "tru";
    private static final String TICKET_PREFIX_ACCOUNT = "acc";
    public static final Object CURRENT_PRINCIPAL =
            new Object() {
                @Override
                public String toString() {
                    return getPrincipal();
                }
            };

    public static Map<String, AuthorizingAnnotationHandler> shiroAnnotations =
            Collections.unmodifiableMap(
                    MCollection.asMap(
                            RequiresPermissions.class.getCanonicalName(),
                                    new PermissionAnnotationHandler(),
                            RequiresRoles.class.getCanonicalName(), new RoleAnnotationHandler(),
                            RequiresAuthentication.class.getCanonicalName(),
                                    new AuthenticatedAnnotationHandler(),
                            RequiresUser.class.getCanonicalName(), new UserAnnotationHandler(),
                            RequiresGuest.class.getCanonicalName(), new GuestAnnotationHandler(),
                            Public.class.getCanonicalName(), new PublicAnnotationHandler()));

    public static boolean hasAccess(String resource) {
        return hasAccess(getSubject(), resource);
    }

    /**
     * Return true if the subject has access to the resource. 
     * In the Background the resource manager will decide if access
     * is granted or not.
     * 
     * Format for the resource is:
     * domain:actions:instances
     * 
     * Examples:
     * 
     * "printer:print:laserjet4400n"
     * "printer:print:*"
     * "printer:print,query:*"
     * "printer" equals "printer:*:*"
     * "printer:*:laserjet4400n"
     * "*"
     * 
     * "user:*"
     * "user:delete"
     * "user:*:12345"
     * "user:update:12345"
     * 
     * @see "https://shiro.apache.org/permissions.html"
     * @param subject
     * @param resource
     * @return True if access is granted
     */
    public static boolean hasAccess(Subject subject, String resource) {
        return M.l(AccessApi.class).getResourceManager().hasAccess(subject, resource);
    }

    public static boolean isAdmin() {
        try {
            Subject subject = M.l(AccessApi.class).getSubject(); // init
            return subject.hasRole(ROLE_ADMIN.value());
        } catch (Throwable t) {
            log.d(t);
            return false;
        }
    }

    public static boolean isAdmin(Subject subject) {
        try {
            return subject.hasRole(ROLE_ADMIN.value());
        } catch (Throwable t) {
            log.d(t);
            return false;
        }
    }
    
    public static Subject getSubject() {
        try {
            Subject subject = M.l(AccessApi.class).getSubject(); // init
            return subject;
        } catch (UnknownSessionException e) {
            M.l(AccessApi.class).destroySession();
            return null;
        } catch (Throwable t) {
            log.d(t);
            return null;
        }
    }

    public static boolean isAuthenticated() {
        try {
            Subject subject = M.l(AccessApi.class).getSubject(); // init
            return subject.isAuthenticated();
        } catch (UnknownSessionException e) {
            M.l(AccessApi.class).destroySession();
            return false;
        } catch (Throwable t) {
            log.d(t);
            return false;
        }
    }

    public static String getPrincipal() {
        try {
            Subject subject = M.l(AccessApi.class).getSubject(); // init
            return getPrincipal(subject);
        } catch (UnknownSessionException e) {
            M.l(AccessApi.class).destroySession();
            return null;
        } catch (Throwable t) {
            log.d(t);
            return null;
        }
    }

    public static String getPrincipal(Subject subject) {
        try {
            Object p = subject.getPrincipal();
            if (p == null) return null;
            return String.valueOf(p);
        } catch (UnknownSessionException e) {
            M.l(AccessApi.class).destroySession();
            return null;
        }
    }

    public static String toString(Subject subject) {
        if (subject == null) return "[null]";
        if (!subject.isAuthenticated()) return "["+USER_GUEST.value()+"]";
        Object p = subject.getPrincipal();
        if (p == null) return "[?]";
        return String.valueOf(p);
    }

    public static void subjectCleanup() {
        ThreadContext.remove();
    }

    public static SubjectEnvironment asAdmin() {
        return asSubject(getAdminSubject());
    }
    
    private static Subject getAdminSubject() {
        return createSubjectWithoutCheck(USER_ADMIN.value());
    }

    public static SubjectEnvironment asSubject(Subject subject) {
        Subject current = ThreadContext.getSubject();
        ThreadContext.bind(subject);
        return new SubjectEnvironment(subject, current);
    }

    public static Collection<Realm> getRealms() {
        try {
            SecurityManager securityManager = M.l(AccessApi.class).getSecurityManager();
            return ((RealmSecurityManager) securityManager).getRealms();
        } catch (Throwable t) {
            log.d(t);
            return Collections.emptyList();
        }
    }

    public static PrincipalData loadPrincipalDataFromRealm(Subject subject) {
        if (!subject.isAuthenticated()) return null;
        for (Realm realm : getRealms()) {
            if (realm instanceof PrincipalDataRealm) {
                Map<String, String> data = ((PrincipalDataRealm) realm).getUserData(subject);
                if (data != null) {
                    data.put(PrincipalData.NAME, String.valueOf(subject.getPrincipal()));
                    if (!data.containsKey(PrincipalData.DISPLAY_NAME))
                        data.put(
                                PrincipalData.DISPLAY_NAME, String.valueOf(subject.getPrincipal()));
                    return new PrincipalData(data);
                }
            }
        }
        return null;
    }

    public static void loadPrincipalData(Subject subject) {
        synchronized (subject) {
            if (subject.getSession().getAttribute(PrincipalData.SESSION_KEY) == null) {
                PrincipalData data = loadPrincipalDataFromRealm(subject);
                if (data == null) {
                    Map<String, String> map = new HashMap<>();
                    map.put(PrincipalData.NAME, String.valueOf(subject.getPrincipal()));
                    map.put(PrincipalData.DISPLAY_NAME, String.valueOf(subject.getPrincipal()));
                    data = new PrincipalData(map);
                }
                subject.getSession().setAttribute(PrincipalData.SESSION_KEY, data);
            }
        }
    }

    public static PrincipalData getPrincipalData(Subject subject) {
        loadPrincipalData(subject);
        return (PrincipalData) subject.getSession().getAttribute(PrincipalData.SESSION_KEY);
    }

    public static PrincipalData getPrincipalData() {
        return getPrincipalData(getSubject());
    }

    public static Subject createSubjectFromSessionId(String sessionId) {
        return new Subject.Builder().sessionId(sessionId).buildSubject();
    }

    public static String getSessionId(boolean create) {
        Session session = getSubject().getSession(create);
        return session == null ? null : String.valueOf(session.getId());
    }

    public static boolean isPermitted(
            List<String> rules, Class<?> permission, String level, Object instance) {
        return isPermitted(
                rules,
                permission == null ? null : permission.getCanonicalName(),
                level,
                instance == null ? null : instance.toString());
    }

    /**
     * Syntax:
     *
     * <p># - comment authenticated !authenticated user: !user: role: !role permission: !permission:
     *
     * <p>In permission the replacements: ${permission} ${level} ${instance}
     *
     * @param rules
     * @param permission
     * @param level
     * @param instance
     * @return true if access is granted
     */
    public static boolean isPermitted(
            List<String> rules, String permission, String level, String instance) {
        // check rules
        Subject subject = getSubject();
        String principal = getPrincipal(subject);
        for (String rule : rules) {
            rule = rule.trim();
            if (rule.isEmpty() || rule.startsWith("#")) continue;
            if (rule.equals("authenticated")) {
                if (!subject.isAuthenticated()) return false;
            } else if (rule.equals("!authenticated")) {
                if (subject.isAuthenticated()) return false;
            } else if (rule.startsWith("user:")) {
                if (!rule.substring(5).equals(principal)) return false;
            } else if (rule.startsWith("!user:")) {
                if (rule.substring(6).equals(principal)) return false;
            } else if (rule.startsWith("role:")) {
                if (!subject.hasRole(rule.substring(5))) return false;
            } else if (rule.startsWith("!role:")) {
                if (subject.hasRole(rule.substring(6))) return false;
            } else if (rule.startsWith("permission:")) {
                String perm = rule.substring(11);
                perm = replacePermission(perm, permission, level, instance);
                if (!subject.isPermitted(new WildcardPermission(perm))) return false;
            } else if (rule.startsWith("!permission:")) {
                String perm = rule.substring(12);
                perm = replacePermission(perm, permission, level, instance);
                if (subject.isPermitted(new WildcardPermission(perm))) return false;
            }
        }
        return true;
    }

    private static String replacePermission(
            String perm, String permission, String level, String instance) {
        if (!perm.contains("${")) return perm;

        permission = normalizeWildcardPart(permission);
        level = normalizeWildcardPart(level);
        instance = normalizeWildcardPart(instance);

        perm = perm.replaceAll("\\${permission}", permission);
        perm = perm.replaceAll("\\${level}", level);
        perm = perm.replaceAll("\\${instance}", instance);

        return perm;
    }

    /**
     * https://shiro.apache.org/permissions.html
     *
     * @param permission Insert a permission, what kind should be checked, e.g. printer, file - or
     *     null
     * @param level Insert a action or level what should be done with the kind - or null
     * @param instance The name of the exact instance, e.g. id or path - or null
     * @return true if access is granted
     */
    public static boolean isPermitted(String permission, String level, String instance) {
        try {
            Subject subject = M.l(AccessApi.class).getSubject(); // init
            permission = normalizeWildcardPart(permission);
            StringBuilder wildcardString = new StringBuilder().append(permission);
            if (level != null || instance != null) {
                if (level == null) wildcardString.append(":*");
                else {
                    level = normalizeWildcardPart(level);
                    wildcardString.append(':').append(level);
                }
                if (instance != null) {
                    instance = normalizeWildcardPart(instance);
                    wildcardString.append(':').append(instance);
                }
            }
            return subject.isPermitted(new WildcardPermission(wildcardString.toString()));
        } catch (Throwable t) {
            log.d(t);
            return false;
        }
    }

    private static String normalizeWildcardPart(String permission) {
        if (permission == null) return "*";
        if (permission.indexOf(':') < 0) return permission;
        return permission.replace(':', '_');
    }

    public static boolean isPermitted(String wildcardString) {
        try {
            Subject subject = M.l(AccessApi.class).getSubject(); // init
            return subject.isPermitted(new WildcardPermission(wildcardString));
        } catch (Throwable t) {
            log.d(t);
            return false;
        }
    }

    public static Locale getLocale() {
        return getLocale(getSubject());
    }

    public static Locale getLocale(Subject subject) {
        Session session = subject.getSession(false);
        if (session != null) {
            Object locale = session.getAttribute(ATTR_LOCALE);
            if (locale != null) {
                if (locale instanceof Locale) return (Locale) locale;
                if (locale instanceof String) return Locale.forLanguageTag((String) locale);
            }
        }
        return Locale.getDefault();
    }

    public static void setLocale(Locale locale) {
        setLocale(getSubject(), locale);
    }

    public static void setLocale(Subject subject, Locale locale) {
        Session session = subject.getSession();
        session.setAttribute(ATTR_LOCALE, locale);
    }

    public static void setLocale(Subject subject, String locale) {
        Session session = subject.getSession();
        session.setAttribute(ATTR_LOCALE, Locale.forLanguageTag(locale));
    }

    public static Object getSessionAttribute(String key) {
        Session session = getSubject().getSession(false);
        if (session == null) return null;
        Object res = session.getAttribute(key);
        if (res != null) return res;
        PrincipalData data = (PrincipalData) session.getAttribute(PrincipalData.SESSION_KEY);
        if (data != null) {}

        return null;
    }

    public static String getSessionAttribute(String key, String def) {
        Object ret = getSessionAttribute(key);
        if (ret == null) return def;
        if (ret instanceof String) return (String) ret;
        return String.valueOf(ret);
    }

    public static Object getSessionAttribute(Subject subject, String key) {
        Session session = subject.getSession(false);
        if (session == null) return null;
        Object res = session.getAttribute(key);
        if (res != null) return res;
        PrincipalData data = (PrincipalData) session.getAttribute(PrincipalData.SESSION_KEY);
        if (data != null) {}

        return null;
    }

    public static String getSessionAttribute(Subject subject, String key, String def) {
        Object ret = getSessionAttribute(subject, key);
        if (ret == null) return def;
        if (ret instanceof String) return (String) ret;
        return String.valueOf(ret);
    }
    
    /**
     * Set an attribute to the current session context.
     * 
     * @param key
     * @param value
     */
    public static void setSessionAttribute(String key, Object value) {
        Session session = getSubject().getSession();
        session.setAttribute(key, value);
    }

    /**
     * Set an attribute to the current session context.
     * @param subject 
     * 
     * @param key
     * @param value
     */
    public static void setSessionAttribute(Subject subject, String key, Object value) {
        Session session = subject.getSession();
        session.setAttribute(key, value);
    }

    /**
     * Login and load user data from source (if needed and possible).
     *
     * @param subject - the subject to login
     * @param user - the user
     * @param pass - the password
     * @param rememberMe
     * @param locale The current locale for the session or null
     * @return true if login was successful
     */
    public static boolean login(
            Subject subject, String user, String pass, boolean rememberMe, Locale locale) {

        try {
            try {
                subject.getSession(true).getAttributeKeys();
            } catch (org.apache.shiro.session.UnknownSessionException e) {
                subject.logout();
            }
        } catch (Throwable t) {
            log.w(t);
        }

        UsernamePasswordToken token = new UsernamePasswordToken(user, MPassword.decode(pass));
        token.setRememberMe(rememberMe);
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            log.d(e);
            return false;
        }
        loadPrincipalData(subject);
        if (locale != null) setLocale(locale);
        return true;
    }

    public static String createTrustTicket(String trust, Subject subject) {
        // TODO encode with rsa
        SecureString password = M.l(TrustApi.class).getPassword(trust);
        return TICKET_PREFIX_TRUST
                + ":"
                + trust
                + ":"
                + Aaa.getPrincipal(subject)
                + ":"
                + password.value();
    }

    public static String createAccountTicket(String account, String password) {
        // TODO encode with rsa
        return TICKET_PREFIX_ACCOUNT + ":" + account + ":" + MPassword.encode(password);
    }

    public static Subject login(String ticket) {
        M.l(AccessApi.class); // init
        if (ticket == null) throw new AuthorizationException("ticket not set");
        String[] parts = ticket.split(":");
        if (parts[0].equals(TICKET_PREFIX_TRUST)) {
            if (parts.length != 4) throw new AuthorizationException("ticket not valide (1)");
            M.l(TrustApi.class).validatePassword(parts[1], parts[3]);
            return new Subject.Builder()
                    .authenticated(true)
                    .principals(new SimplePrincipalCollection(parts[2], REALM_TRUST.value()))
                    .buildSubject();
        }
        if (parts[0].equals(TICKET_PREFIX_ACCOUNT)) {
            if (parts.length != 3) throw new AuthorizationException("ticket not valide (2)");
            Subject subject = M.l(AccessApi.class).createSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(parts[1], parts[2]);
            subject.login(token);
            return subject;
        }
        throw new AuthorizationException("unknown ticket type");
    }

    /**
     * Create a plain subject without authentication. 
     * @param account
     * @return A new Subject
     */
    public static Subject createSubjectWithoutCheck(String account) {
        return new Subject.Builder()
                .authenticated(true)
                .principals(new SimplePrincipalCollection(account, REALM_TRUST.value()))
                .buildSubject();
    }

    public static boolean hasRole(String role) {
        return getSubject().hasRole(role);
    }

    public static boolean hasRole(Subject subject, String role) {
        return subject.hasRole(role);
    }
    
    public static boolean hasPermission(Subject subject, Class<?> clazz) {
        return hasPermission(subject, clazz.getAnnotations());
    }

    public static boolean hasPermission(Subject subject, Method method) {
        return hasPermission(subject, method.getAnnotations());
    }

    public static boolean hasPermission(Subject subject, Annotation[] annotations) {
        Value<Boolean> perm = new Value<>(true);
        subject.execute(
                () -> {
                    try {
                        for (Annotation classAnno : annotations) {
                            AuthorizingAnnotationHandler handler =
                                    shiroAnnotations.get(classAnno.getClass().getCanonicalName());
                            if (handler == null) continue;
                            handler.assertAuthorized(classAnno);
                        }
                    } catch (AuthorizationException e) {
                        perm.value = false;
                    }
                });
        return perm.value;
    }

    public static void checkPermission(Object obj) throws AuthorizationException {
        if (obj == null) return;
        checkPermission(obj.getClass());
    }

    public static void checkPermission(Class<?> clazz) throws AuthorizationException {
        // TODO add caching
        checkPermission(clazz.getAnnotations());
    }

    public static void checkPermission(Method method) throws AuthorizationException {
        checkPermission(method.getAnnotations());
    }

    public static void checkPermission(Annotation[] annotations) throws AuthorizationException {
        for (Annotation classAnno : annotations) {
            AuthorizingAnnotationHandler handler =
                    shiroAnnotations.get(classAnno.annotationType().getCanonicalName());
            if (handler == null) continue;
            handler.assertAuthorized(classAnno);
        }
    }

    public static boolean isAnnotated(Class<?> clazz) {
        // TODO add caching
        return isAnnotated(clazz.getAnnotations());
    }

    public static boolean isAnnotated(Method method) {
        return isAnnotated(method.getAnnotations());
    }

    public static boolean isAnnotated(Annotation[] annotations) {
        for (Annotation classAnno : annotations) {
            AuthorizingAnnotationHandler handler =
                    shiroAnnotations.get(classAnno.annotationType().getCanonicalName());
            if (handler == null) continue;
            return true;
        }
        return false;
    }

    public static String createBearerToken(Subject subject, String issuer) throws ShiroException {
        for (Realm realm : Aaa.getRealms()) {
            if (realm instanceof BearerRealm) {
                return ((BearerRealm) realm).createBearerToken(Aaa.getSubject(), issuer);
            }
        }
        return null;
    }

    public static String createBearerToken(
            Subject subject, String issuer, BearerConfiguration configuration)
            throws ShiroException {
        for (Realm realm : Aaa.getRealms()) {
            if (realm instanceof BearerRealm) {
                return ((BearerRealm) realm)
                        .createBearerToken(Aaa.getSubject(), issuer, configuration);
            }
        }
        return null;
    }

    // for individual access lists
    public static boolean hasAccessByList(String list, Subject account, String objectIdent) {
        List<String> map = MCollection.toList(list.split(";"));
        return hasAccessByList(map, account, objectIdent);
    }

    // for individual access lists
    public static boolean hasAccessByList(List<String> map, Subject account, String objectIdent) {
        boolean access = false;
        String principal = getPrincipal(account);
        for (String g : map) {
            
            g = g.trim();
            if (g.length() == 0) continue;
            
            if (g.startsWith("policy:")) {
                access = MCast.toboolean(g.substring(7), access);
            } else
            if (g.startsWith("user:")) {
                if (g.substring(5).equals( principal ) ) {
                    log.d("access granted",objectIdent,g);
                    access = true;
                    break;
                }
            } else
            if (g.startsWith("notuser:")) {
                if (g.substring(8).equals( principal ) ) {
                    log.d("access denied",objectIdent,g);
                    access = false;
                    break;
                }
            } else
            if (g.startsWith("not:")) {
                if ( account.hasRole(g.substring(4)) ) {
                    log.d("access denied",objectIdent,g);
                    access = false;
                    break;
                }
            } else
            if (g.equals("*")) {
                log.d("access granted",objectIdent,g);
                access = true;
                break;
            } else
            if ( account.hasRole(g) ) {
                log.d("access granted",objectIdent,g);
                access = true;
                break;
            };
        }
        return access;
    }
}
