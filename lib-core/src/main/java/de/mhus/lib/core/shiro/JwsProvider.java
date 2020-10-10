package de.mhus.lib.core.shiro;

import de.mhus.lib.annotations.activator.DefaultImplementation;

@DefaultImplementation(JwsProviderImpl.class)
public interface JwsProvider {

    String createBearerToken(String username, BearerConfiguration configuration);

    JwsData readToken(String tokenStr);

    String getSubject(String tokenStr);

}
