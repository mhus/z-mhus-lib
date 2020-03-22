package de.mhus.lib.core.shiro;

import java.util.Collection;
import java.util.Locale;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

import de.mhus.lib.core.M;

public class ShiroUtil {

    public static final String ROLE_ADMIN = "GLOBAL_ADMIN";
    private static final Object ATTR_LOCALE = "locale";

    public static boolean isAdmin() {
        Subject subject =  M.l(ShiroSecurity.class).getSubject(); // init
        return subject.hasRole(ROLE_ADMIN);
    }
    
    public static Subject getSubject() {
        Subject subject =  M.l(ShiroSecurity.class).getSubject(); // init
        return subject;
    }
    
    public static boolean isAuthenticated() {
        Subject subject =  M.l(ShiroSecurity.class).getSubject(); // init
        return subject.isAuthenticated();
    }
    
    public static String getPrincipal() {
        Subject subject =  M.l(ShiroSecurity.class).getSubject(); // init
        return getPrincipal(subject);
    }
    
    public static String getPrincipal(Subject subject) {
        Object p = subject.getPrincipal();
        if (p == null) return null;
        return String.valueOf(p);
    }

    public static String toString(Subject subject) {
        if (subject == null) return "null";
        if (!subject.isAuthenticated()) return "[guest]";
        Object p = subject.getPrincipal();
        if (p == null) return "[?]";
        return String.valueOf(p);
    }

    public static void subjectCleanup() {
        ThreadContext.remove();
    }

    public static SubjectEnvironment useSubject(Subject subject) {
        Subject current = ThreadContext.getSubject();
        ThreadContext.bind(subject);
        return new SubjectEnvironment(subject, current);
    }
    
    public static Collection<Realm> getRealms() {
        SecurityManager securityManager = M.l(ShiroSecurity.class).getSecurityManager();
        return ((RealmSecurityManager)securityManager).getRealms();
    }
    
    public static PrincipalData loadPrincipalDataFromRealm(Subject subject) {
        if (!subject.isAuthenticated()) return null;
        for (Realm realm : getRealms()) {
            if (realm instanceof PrincipalDataRealm) {
                PrincipalData data = ((PrincipalDataRealm)realm).getUserData(subject);
                if (data != null) {
                    data.put(PrincipalData.NAME, String.valueOf(subject.getPrincipal()));
                    if (!data.containsKey(PrincipalData.DISPLAY_NAME))
                        data.put(PrincipalData.DISPLAY_NAME, String.valueOf(subject.getPrincipal()));
                    data.ro();
                    return data;
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
                    data = new PrincipalData();
                    data.put(PrincipalData.NAME, String.valueOf(subject.getPrincipal()));
                    data.put(PrincipalData.DISPLAY_NAME, String.valueOf(subject.getPrincipal()));
                    data.ro();
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

    public static boolean checkPermission(String permission) {
        Subject subject =  M.l(ShiroSecurity.class).getSubject(); // init
        try {
            subject.checkPermission(new WildcardPermission(permission));
            return true;
        } catch (AuthorizationException e) {
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
                if (locale instanceof Locale)
                    return (Locale)locale;
                if (locale instanceof String)
                    return Locale.forLanguageTag((String)locale);
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
    
}
