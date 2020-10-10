package de.mhus.lib.core.shiro;

import org.apache.shiro.ShiroException;
import org.apache.shiro.subject.Subject;

public interface BearerRealm {

    final public BearerConfiguration DEFAULT_CONFIGURATION = new BearerConfiguration();

    /**
     * Create a new token.
     * @param subject A subject of the current realm
     * @param configuration Configuration. You could recycle an existing configuration.
     * @return A new bearer token to use with org.apache.shiro.authc.BearerToken
     * @throws ShiroException It account is not part of the realm or the token can not be created
     */
    String createBearerToken(Subject subject, BearerConfiguration configuration) throws ShiroException;

    default String createBearerToken(Subject subject) {
        return createBearerToken(subject, DEFAULT_CONFIGURATION);
    }

}
