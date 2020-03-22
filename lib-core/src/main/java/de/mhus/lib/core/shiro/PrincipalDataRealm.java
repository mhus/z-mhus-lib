package de.mhus.lib.core.shiro;

import org.apache.shiro.subject.Subject;

public interface PrincipalDataRealm {

    PrincipalData getUserData(Subject subject);

}
