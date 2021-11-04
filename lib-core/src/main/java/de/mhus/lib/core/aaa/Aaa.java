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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.Permission;
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
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

import de.mhus.lib.annotations.generic.Public;
import de.mhus.lib.annotations.lang.Function0;
import de.mhus.lib.core.M;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MCollection;
import de.mhus.lib.core.MPassword;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.cache.CacheConfig;
import de.mhus.lib.core.cache.ICache;
import de.mhus.lib.core.cache.ICacheService;
import de.mhus.lib.core.cfg.CfgBoolean;
import de.mhus.lib.core.cfg.CfgInt;
import de.mhus.lib.core.cfg.CfgLong;
import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.core.logging.ITracer;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.security.TrustApi;
import de.mhus.lib.core.util.Value;
import io.opentracing.Scope;

public class Aaa {

    // default rights
    public static final String READ = "read";
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    public static final String VIEW = "view";
    public static final String ADMIN = "admin";

    public static final CfgString USER_ADMIN = new CfgString(AccessApi.class, "adminUser", "admin");
    public static final CfgString USER_GUEST = new CfgString(AccessApi.class, "guestUser", "guest");

    private static final CfgBoolean CFG_USE_ACCESS_CACHE =
            new CfgBoolean(
                    AccessApi.class, "accessCacheEnabled", false); // to false for debug reasons
    private static final CfgInt CFG_ACCESS_CACHE_SIZE =
            new CfgInt(AccessApi.class, "accessCacheSize", 1000000);
    private static final CfgLong CFG_ACCESS_CACHE_TTL =
            new CfgLong(AccessApi.class, "accessCacheTTL", MPeriod.MINUTE_IN_MILLISECOUNDS * 15);

    private static final Log log = Log.getLog(Aaa.class);
    public static final CfgString ROLE_ADMIN =
            new CfgString(AccessApi.class, "adminRole", "GLOBAL_ADMIN");
    private static final String ATTR_LOCALE = "locale";
    public static final String TICKET_PREFIX_TRUST = "tru";
    public static final String TICKET_PREFIX_ACCOUNT = "acc";
    public static final String TICKET_PREFIX_BEARER = "jwt";
    public static final Object CURRENT_PRINCIPAL_OR_GUEST =
            new Object() {
                @Override
                public String toString() {
                    return getPrincipalOrGuest();
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
    private static ICache<String, Boolean> accessCacheApi;

    public static final SimpleAccount ACCOUNT_ADMIN =
            new SimpleAccount(USER_ADMIN.value(), UUID.randomUUID().toString(), "");
    public static final SimpleAccount ACCOUNT_GUEST =
            new SimpleAccount(USER_GUEST.value(), UUID.randomUUID().toString(), "");

    private static final CfgString PERMS_GUEST_INT =
            new CfgString(
                            AccessApi.class,
                            "permsGuestInt",
                            "de.mhus.lib.core.aaa.TrustedToken:admin:de.mhus.karaf.commands.impl.CmdAccessAdmin;"
                                    + "de.mhus.lib.core.aaa.TrustedToken:*:de.mhus.karaf.commands.impl.CmdAccessLogin;"
                                    + "de.mhus.lib.core.aaa.TrustedToken:*:de.mhus.lib.core.schedule.SchedulerJob;"
                                    + "de.mhus.lib.core.aaa.TrustedToken:*:de.mhus.osgi.dev.dev.CmdAccessTool;"
                                    + "de.mhus.lib.core.aaa.TrustedToken:*:de.mhus.lib.jms.ServerJms;"
                                    + "de.mhus.lib.core.aaa.TrustedToken:*:de.mhus.lib.core.aaa.TrustedAaa;"
                                    + "de.mhus.lib.core.aaa.TrustedToken:*:de.mhus.rest.core.impl.RestServlet")
                    .updateAction(v -> updateGuestPerms());
    public static final CfgString PERMS_GUEST =
            new CfgString(AccessApi.class, "permsGuest", "").updateAction(v -> updateGuestPerms());

    public static final CfgString ROLES_GUEST =
            new CfgString(AccessApi.class, "rolesGuest", "")
                    .updateAction(
                            v -> {
                                if (v != null) {
                                    HashSet<String> roles = new HashSet<>();
                                    for (String r : v.split(";"))
                                        if (MString.isSetTrim(r)) roles.add(r.trim());
                                    ACCOUNT_GUEST.setRoles(roles);
                                }
                            });

    public static final CfgString ROLES_ADMIN =
            new CfgString(AccessApi.class, "rolesAdmin", "")
                    .updateAction(
                            v -> {
                                if (v != null) {
                                    HashSet<String> roles = new HashSet<>();
                                    roles.add(Aaa.ROLE_ADMIN.value());
                                    for (String r : v.split(";"))
                                        if (MString.isSetTrim(r)) roles.add(r.trim());
                                    ACCOUNT_ADMIN.setRoles(roles);
                                }
                            });

    public static final CfgBoolean ADMIN_LOGIN_ALLOWED =
            new CfgBoolean(AccessApi.class, "allowAdminLogin", false);

    private static Subject DUMMY_SUBJECT = null;
    private static Subject GUEST_SUBJECT;
    private static boolean doInitGuestSubject;

    static {
        ACCOUNT_ADMIN.addStringPermission("*");
        ROLES_ADMIN.doUpdateAction();

        PERMS_GUEST.doUpdateAction();
        ROLES_GUEST.doUpdateAction();
    }

    private static synchronized void updateGuestPerms() {
        HashSet<Permission> perms = new HashSet<>();
        {
            String v = PERMS_GUEST_INT.value();
            if (MString.isSetTrim(v)) {
                for (String r : v.split(";"))
                    if (MString.isSetTrim(r)) perms.add(new WildcardPermission(r.trim()));
            }
        }
        {
            String v = PERMS_GUEST.value();
            if (MString.isSetTrim(v)) {
                for (String r : v.split(";"))
                    if (MString.isSetTrim(r)) perms.add(new WildcardPermission(r.trim()));
            }
        }
        ACCOUNT_GUEST.setObjectPermissions(perms);
        GUEST_SUBJECT = null;
    }

    public static boolean hasAccess(Class<?> domain, String action, String instance) {
        return hasAccess(
                getSubject(),
                domain.getCanonicalName()
                        + ":"
                        + (action == null ? "*" : normalize(action))
                        + (instance != null ? ":" + normalize(instance) : ""));
    }

    public static boolean hasAccess(String domain, String action, String instance) {
        return hasAccess(
                getSubject(),
                normalize(domain)
                        + ":"
                        + (action == null ? "*" : normalize(action))
                        + (instance != null ? ":" + normalize(instance) : ""));
    }

    public static boolean hasAccess(String resource) {
        return hasAccess(getSubject(), resource);
    }

    public static boolean hasAccess(
            Subject subject, String domain, String action, String instance) {
        return hasAccess(
                subject,
                normalize(domain)
                        + ":"
                        + (action == null ? "*" : normalize(action))
                        + (instance != null ? ":" + normalize(instance) : ""));
    }

    public static boolean hasAccess(
            Subject subject, Class<?> domain, String action, String instance) {
        return hasAccess(
                subject,
                domain.getCanonicalName()
                        + ":"
                        + (action == null ? "*" : normalize(action))
                        + (instance != null ? ":" + normalize(instance) : ""));
    }
    /**
     * Return true if the subject has access to the resource. In the Background the resource manager
     * will decide if access is granted or not.
     *
     * <p>Format for the resource is: domain:actions:instances
     *
     * <p>Examples:
     *
     * <p>"printer:print:laserjet4400n" "printer:print:*" "printer:print,query:*" "printer" equals
     * "printer:*:*" "printer:*:laserjet4400n" "*"
     *
     * <p>"user:*" "user:delete" "user:*:12345" "user:update:12345"
     *
     * @see "https://shiro.apache.org/permissions.html"
     * @param subject
     * @param resource
     * @return True if access is granted
     */
    public static boolean hasAccess(Subject subject, String resource) {
        if (subject.getPrincipal() == null) subject = getGuestSubject(false);
        touch(subject);
        Boolean cached = getCachedAccess(subject, "access", resource);
        if (cached != null) return cached;

        boolean value = subject.isPermitted(resource);
        doCacheAccess(subject, "access", resource, value);
        return value;
    }

    private static synchronized Subject getGuestSubject(boolean careful) {
        if (GUEST_SUBJECT == null || !isPrincipal(GUEST_SUBJECT, careful)) {
            if (careful) return null;
            if (doInitGuestSubject) {
                MApi.dirtyLogDebug("Aaa.getGuestSubject prevent infinitiy loop - return null");
                return null;
            }
            try {
                doInitGuestSubject = true;
                GUEST_SUBJECT = M.l(AccessApi.class).createSubject();
                if (GUEST_SUBJECT == null || USER_GUEST == null || USER_GUEST.value() == null) {
                    MApi.dirtyLogDebug(
                            "Aaa.getGuestSubject can't initialize guest subject - return null");
                    GUEST_SUBJECT = null;
                    return null;
                }
                GUEST_SUBJECT.login(new TrustedToken(USER_GUEST.value()));
            } catch (Throwable t) {
                MApi.dirtyLogDebug(
                        "Aaa.getGuestSubject can't initialize guest subject - return null: " + t);
                MApi.dirtyLogDebug(t);
                GUEST_SUBJECT = null;
                return null;
            } finally {
                doInitGuestSubject = false;
            }
        }
        return GUEST_SUBJECT;
    }

    private static void doCacheAccess(
            Subject subject, String action, String resource, boolean value) {
        if (!CFG_USE_ACCESS_CACHE.value()) return;
        initAccessCache();
        if (accessCacheApi == null) return;
        accessCacheApi.put(subject.getPrincipal() + ":" + action + "@" + resource, value);
    }

    private static synchronized void initAccessCache() {
        if (accessCacheApi != null) return;
        try {
            ICacheService cacheService = M.l(ICacheService.class);
            accessCacheApi =
                    cacheService.createCache(
                            new Aaa(),
                            "aaaAccess",
                            String.class,
                            Boolean.class,
                            new CacheConfig()
                                    .setHeapSize(CFG_ACCESS_CACHE_SIZE.value())
                                    .setTTL(CFG_ACCESS_CACHE_TTL.value()));
        } catch (Throwable t) {
            MApi.dirtyLogDebug("Aaa:initAccessCache", t.toString());
        }
    }

    private static Boolean getCachedAccess(Subject subject, String action, String resource) {
        if (!CFG_USE_ACCESS_CACHE.value()) return null;
        initAccessCache();
        if (accessCacheApi == null) return null;
        return accessCacheApi.get(subject.getPrincipal() + ":" + action + "@" + resource);
    }

    public static boolean isAdmin() {
        try {
            Subject subject = getSubject(); // init
            if (subject == null) return false;
            Boolean cached = getCachedAccess(subject, "admin", "");
            if (cached != null) return cached;
            boolean value = subject.hasRole(ROLE_ADMIN.value());
            doCacheAccess(subject, "admin", "", value);
            return value;
        } catch (Throwable t) {
            log.d(t);
            return false;
        }
    }

    public static boolean isAdmin(Subject subject) {
        try {
            Boolean cached = getCachedAccess(subject, "admin", "");
            if (cached != null) return cached;
            boolean value = subject.hasRole(ROLE_ADMIN.value());
            doCacheAccess(subject, "admin", "", value);
            return value;
        } catch (Throwable t) {
            log.d(t);
            return false;
        }
    }

    public static Subject getSubject() {
        try {
            SecurityUtils
                    .getSecurityManager(); // not initialized,  M.l() will loop or fail with NPE
            // Subject subject = M.l(AccessApi.class).getSubject(); // cause a infinity loop in
            // M.l()
            Subject subject = SecurityUtils.getSubject();
            return subject;
        } catch (org.apache.shiro.UnavailableSecurityManagerException e) {
            // not initialized,  M.l() will loop or fail with NPE
            //            log.d(e.toString()); log causes an NPE
            //            log.t(e);
            MApi.dirtyLogDebug(e.toString());
            return null;
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
            Subject subject = getSubject(); // init
            return subject != null && subject.isAuthenticated();
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
            Subject subject = getSubject(); // init
            if (subject == null) return null;
            return getPrincipal(subject);
        } catch (UnknownSessionException e) {
            M.l(AccessApi.class).destroySession();
            return null;
        } catch (Throwable t) {
            log.d(t);
            return null;
        }
    }

    public static String getPrincipalOrGuest() {
        try {
            Subject subject = getSubject(); // init
            if (subject == null) return USER_GUEST.value();
            String ret = getPrincipal(subject);
            if (ret == null) return USER_GUEST.value();
            return ret;
        } catch (UnknownSessionException e) {
            M.l(AccessApi.class).destroySession();
            return USER_GUEST.value();
        } catch (Throwable t) {
            log.d(t);
            return USER_GUEST.value();
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

    public static boolean isPrincipal(Subject subject, boolean careful) {
        if (subject == null) return false;
        try {
            Object p = subject.getPrincipal();
            if (p == null) return false;
            return true;
        } catch (UnknownSessionException e) {
            if (!careful) M.l(AccessApi.class).destroySession();
            return false;
        }
    }

    public static String toString(Subject subject) {
        if (subject == null) return "[null]";
        if (!subject.isAuthenticated()) return USER_GUEST.value();
        Object p = subject.getPrincipal();
        if (p == null) return "[?]";
        return String.valueOf(p);
    }

    public static void subjectCleanup() {
        ThreadContext.remove();
    }

    public static SubjectEnvironment asAdmin() {
        Subject subject = createSubjectWithoutCheck(USER_ADMIN.value());
        return asSubject(subject);
    }

    public static SubjectEnvironment asSubject(String username) {
        if (username == null) username = USER_GUEST.value();
        Subject subject = createSubjectWithoutCheck(username);
        return asSubject(subject);
    }

    public static SubjectEnvironment asSubject(Subject subject) {
        Scope scope =
                ITracer.get()
                        .enter(
                                "asSubject " + subject.getPrincipal(),
                                "username",
                                subject.getPrincipal());
        Subject current = ThreadContext.getSubject();
        ThreadContext.bind(subject);
        return new SubjectEnvironment(subject, current, scope);
    }

    public static SubjectEnvironment asSubjectWithoutTracing(Subject subject) {
        Subject current = ThreadContext.getSubject();
        ThreadContext.bind(subject);
        return new SubjectEnvironment(subject, current, null);
    }

    /**
     * Run as subject or if subject is null use the dummy subject.
     *
     * @param subject
     * @return Closeable environment object
     */
    public static SubjectEnvironment asSubjectOrAnonymous(Subject subject) {
        if (subject == null) {
            if (DUMMY_SUBJECT == null) {
                DUMMY_SUBJECT = createNewSubject();
            }
            subject = DUMMY_SUBJECT;
        }
        Scope scope =
                ITracer.get()
                        .enter(
                                "asSubject " + subject.getPrincipal(),
                                "username",
                                subject.getPrincipal());
        Subject current = ThreadContext.getSubject();
        ThreadContext.bind(subject);
        return new SubjectEnvironment(subject, current, scope);
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
        Subject subject = getSubject();
        if (subject == null) subject = getGuestSubject(false);
        return isPermitted(subject, permission, level, instance);
    }

    public static boolean isPermitted(
            Subject subject, String permission, String level, String instance) {
        touch(subject);
        try {
            if (subject == null) return false;
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
        Subject subject = getSubject();
        if (subject == null) subject = getGuestSubject(false);
        return isPermitted(subject, wildcardString);
    }

    public static boolean isPermitted(Subject subject, String wildcardString) {
        touch(subject);
        try {
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
        if (subject != null) {
            Session session = subject.getSession(false);
            if (session != null) {
                Object locale = session.getAttribute(ATTR_LOCALE);
                if (locale != null) {
                    if (locale instanceof Locale) return (Locale) locale;
                    if (locale instanceof String) return Locale.forLanguageTag((String) locale);
                }
            }
        }
        return Locale.getDefault();
    }

    public static void setLocale(Locale locale) {
        setLocale(getSubject(), locale);
    }

    public static void setLocale(Subject subject, Locale locale) {
        if (subject == null) return;
        Session session = subject.getSession();
        session.setAttribute(ATTR_LOCALE, locale);
    }

    public static void setLocale(Subject subject, String locale) {
        if (subject == null) return;
        Session session = subject.getSession();
        session.setAttribute(ATTR_LOCALE, Locale.forLanguageTag(locale));
    }

    public static Object getSessionAttribute(String key) {
        Subject subject = getSubject();
        if (subject == null) subject = getGuestSubject(false);
        Session session = subject.getSession(false);
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
        if (subject == null) return null;
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
        Subject subject = getSubject();
        if (subject == null) return;
        Session session = subject.getSession();
        session.setAttribute(key, value);
    }

    /**
     * Set an attribute to the current session context.
     *
     * @param subject
     * @param key
     * @param value
     */
    public static void setSessionAttribute(Subject subject, String key, Object value) {
        if (subject == null) return;
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
            // do not need a proper environment for username tokens
            subject.login(token);
        } catch (AuthenticationException e) {
            log.d(e);
            return false;
        }
        loadPrincipalData(subject);
        if (locale != null) setLocale(locale);
        return true;
    }

    public static String createAccountTicket(String account, String password) {
        // TODO encode with rsa
        return TICKET_PREFIX_ACCOUNT + ":" + account + ":" + MPassword.encode(password);
    }

    public static Subject login(String ticket) {
        return properEnvironment(
                () -> {
                    AuthenticationToken token = createToken(ticket);
                    Subject subject = M.l(AccessApi.class).createSubject();
                    subject.login(token);
                    return subject;
                },
                false);
    }

    public static void login(Subject subject, AuthenticationToken authToken) {
        properEnvironment(
                () -> {
                    subject.login(authToken);
                    return null;
                },
                false);
    }

    public static AuthenticationToken createToken(String ticket) {
        M.l(AccessApi.class); // init
        if (ticket == null) throw new AuthorizationException("ticket not set");
        // BEGIN legacy 6.x
        if (ticket.startsWith("tru,")) {
            String[] parts = ticket.split(",");
            // tru,trust name,trust pw unsecure,username
            ticket = parts[0] + ":" + parts[1] + ":" + parts[3] + ":" + parts[2];
        }
        // END legacy
        int p = ticket.indexOf(':');
        if (p < 0) throw new AuthorizationException("ticket not valide (3)");
        String type = ticket.substring(0, p);

        if (type.equals(TICKET_PREFIX_TRUST)) {
            String[] parts = ticket.split(":", 4);
            if (parts.length != 4) throw new AuthorizationException("ticket not valide (1)");
            return M.l(TrustApi.class).createToken(ticket.substring(p + 1));
        }
        if (type.equals(TICKET_PREFIX_ACCOUNT)) {
            String[] parts = ticket.split(":", 3);
            if (parts.length != 3) throw new AuthorizationException("ticket not valide (2)");
            UsernamePasswordToken token =
                    new UsernamePasswordToken(parts[1], MPassword.decode(parts[2]));
            return token;
        }
        if (type.equals(TICKET_PREFIX_BEARER)) {
            String bearer = ticket.substring(p + 1);
            BearerToken token = new BearerToken(bearer);
            return token;
        }
        throw new AuthorizationException("unknown ticket type");
    }
    /**
     * Create a plain subject without authentication.
     *
     * @param account
     * @return A new Subject
     */
    public static Subject createSubjectWithoutCheck(String account) {
        try (Scope scope =
                ITracer.get().enter("createSubjectWithoutCheck " + account, "username", account)) {
            return properEnvironment(
                    () -> {
                        Subject subject = M.l(AccessApi.class).createSubject();
                        subject.login(new TrustedToken(account));
                        return subject;
                    },
                    false);
        }
    }

    public static boolean hasRole(String role) {
        Subject subject = getSubject();
        if (subject.getPrincipal() == null) subject = getGuestSubject(false);
        touch(subject);
        return subject.hasRole(role);
    }

    public static boolean hasRole(Subject subject, String role) {
        if (subject.getPrincipal() == null) subject = getGuestSubject(false);
        return subject.hasRole(role);
    }

    public static boolean hasPermission(Subject subject, Class<?> clazz) {
        return hasPermission(subject, clazz.getAnnotations());
    }

    public static boolean hasPermission(Subject subject, Method method) {
        return hasPermission(subject, method.getAnnotations());
    }

    public static boolean hasPermission(Subject subject, Annotation[] annotations) {

        if (subject.getPrincipal() == null) subject = getGuestSubject(false);
        touch(subject);
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

        touch();
        properEnvironment(
                () -> {
                    for (Annotation classAnno : annotations) {
                        AuthorizingAnnotationHandler handler =
                                shiroAnnotations.get(classAnno.annotationType().getCanonicalName());
                        if (handler == null) continue;
                        handler.assertAuthorized(classAnno);
                    }
                    return null;
                },
                true); // must be careful otherwise startup in osgi will fail
    }

    /**
     * Do the action even as the current user or guest environment. An anonymous environment will be
     * surrounded with guest.
     *
     * @param <R> Return type
     * @param action The action to do
     * @param careful Set to careful will not create anything
     * @return Return of the action
     */
    public static <R> R properEnvironment(Function0<R> action, boolean careful) {

        // check if we are in anonymous environment
        final Subject subject = getSubject();
        if (!isPrincipal(subject, careful)) {
            // surround with guest environment
            final Subject guest = getGuestSubject(careful);
            if (guest == null) return action.apply(); // fallback to prevent loops
            try (SubjectEnvironment access = asSubjectWithoutTracing(guest)) {
                return action.apply();
            }
        }
        // leave existing
        return action.apply();
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
        if (account.getPrincipal() == null) account = getGuestSubject(false);
        String principal = getPrincipal(account);
        for (String g : map) {

            g = g.trim();
            if (g.length() == 0) continue;

            if (g.startsWith("policy:")) {
                access = MCast.toboolean(g.substring(7), access);
            } else if (g.startsWith("user:")) {
                if (g.substring(5).equals(principal)) {
                    log.d("access granted", objectIdent, g);
                    access = true;
                    break;
                }
            } else if (g.startsWith("notuser:")) {
                if (g.substring(8).equals(principal)) {
                    log.d("access denied", objectIdent, g);
                    access = false;
                    break;
                }
            } else if (g.startsWith("not:")) {
                if (account.hasRole(g.substring(4))) {
                    log.d("access denied", objectIdent, g);
                    access = false;
                    break;
                }
            } else if (g.equals("*")) {
                log.d("access granted", objectIdent, g);
                access = true;
                break;
            } else if (account.hasRole(g)) {
                log.d("access granted", objectIdent, g);
                access = true;
                break;
            }
            ;
        }
        return access;
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
        touch(subject);
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

    public static String normalize(String action) {
        if (action == null) return "";
        if (action.contains(":")) return action.replace(':', '_');
        return action;
    }

    public static Subject createNewSubject() {
        Subject subject = M.l(AccessApi.class).createSubject();
        return subject;
    }

    public static Collection<String> getPerms(Subject subject) {
        return TrustedAaa.getPerms(subject);
    }

    /**
     * Touch the current session
     */
    public static void touch() {
        try {
            Subject subject = getSubject();
            if (subject == null) return;
            Session session = subject.getSession(false);
            if (session == null) return;
            session.touch();
        } catch (Throwable t) {
            log.d(t);
        }
    }
    
    /**
     * Touch the session of the subject
     * @param subject 
     */
    public static void touch(Subject subject) {
        try {
            if (subject == null) return;
            Session session = subject.getSession(false);
            if (session == null) return;
            session.touch();
        } catch (Throwable t) {
            log.d(t);
        }
    }

    @SuppressWarnings("unchecked")
    public static Collection<String> getRoles(Subject subject) {
        if (subject instanceof SimpleAccount)
            return ((SimpleAccount)subject).getRoles();
        return (Collection<String>) M.EMPTY_LIST;
    }

}
