package de.mhus.lib.core.shiro;

import java.util.Map;

import org.apache.shiro.subject.Subject;

public interface PrincipalDataRealm {

    Map<String,String> getUserData(Subject subject);

}
