package de.mhus.lib.core.shiro;

import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

import de.mhus.lib.core.M;

public class ShiroUtil {

    public static final String ROLE_ADMIN = "GLOBAL_ADMIN";

    public static boolean isAdmin() {
        M.l(ShiroSecurity.class); // init
        Subject subject = SecurityUtils.getSubject();
        return subject.hasRole(ROLE_ADMIN);
    }
    
    public static Subject getSubject() {
        M.l(ShiroSecurity.class); // init
        return SecurityUtils.getSubject();
    }
    
    public static boolean isAuthenticated() {
        M.l(ShiroSecurity.class); // init
        Subject subject = SecurityUtils.getSubject();
        return subject.isAuthenticated();
    }
    
    public static String getPrincipal() {
        M.l(ShiroSecurity.class); // init
        Subject subject = SecurityUtils.getSubject();
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
    
    public static Collection<Realm> getReals() {
        SecurityManager securityManager = M.l(ShiroSecurity.class).getSecurityManager();
        return ((RealmSecurityManager)securityManager).getRealms();
    }

}
