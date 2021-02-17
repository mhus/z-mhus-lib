package de.mhus.lib.core.shiro;

import org.apache.shiro.subject.Subject;

public interface ResourceManager {

    boolean hasAccess(Subject subject, String object);

}
