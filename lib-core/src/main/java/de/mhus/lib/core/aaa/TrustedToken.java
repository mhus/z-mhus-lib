package de.mhus.lib.core.aaa;

import org.apache.shiro.authc.AuthenticationToken;

public class TrustedToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;
    private String principal;

    public TrustedToken(String principal) {
        this.principal = principal;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

}
