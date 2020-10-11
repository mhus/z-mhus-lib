package de.mhus.lib.core.shiro;

import de.mhus.lib.annotations.activator.DefaultImplementation;

@DefaultImplementation(JwtProviderImpl.class)
public interface JwtProvider {

    String createBearerToken(String username, String issuer, BearerConfiguration configuration);

    JwsData readToken(String tokenStr);

    String getSubject(String tokenStr);

}
