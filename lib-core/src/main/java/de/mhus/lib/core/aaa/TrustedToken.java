package de.mhus.lib.core.aaa;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * To use TrustedToken you need to give access to one of the calling classes in the stacktrace to access the
 * user or all users. Give access to
 * 
 * de.mhus.lib.core.aaa.TrustedToken:admin:de.mhus.karaf.commands.impl.CmdAccessAdmin
 * 
 * to let de.mhus.karaf.commands.impl.CmdAccessAdmin access 'admin'
 * or
 * 
 * de.mhus.lib.core.aaa.TrustedToken:*:de.mhus.karaf.commands.impl.CmdAccessLogin
 * 
 * to let de.mhus.karaf.commands.impl.CmdAccessLogin access all users (this is default).
 * 
 * 
 * @author mikehummel
 *
 */
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
