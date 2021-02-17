package de.mhus.lib.core.shiro;

import org.apache.shiro.subject.Subject;

public class DefaultResourceManager implements ResourceManager {

    @Override
    public boolean hasAccess(Subject subject, String object) {
        return subject.hasRole("RESOURCE_" + object.toUpperCase());
    }

}
