package de.mhus.lib.core.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;

import de.mhus.lib.annotations.activator.DefaultImplementation;

@DefaultImplementation(DefaultShiroSecurity.class)
public interface ShiroSecurity {

    SecurityManager getSecurityManager();

    Subject createSubject();

    void updateSessionLastAccessTime();

    Subject getSubject();

}
