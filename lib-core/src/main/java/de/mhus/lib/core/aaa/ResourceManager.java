package de.mhus.lib.core.aaa;

import org.apache.shiro.subject.Subject;

public interface ResourceManager {

    boolean hasAccess(Subject subject, String object);

}
