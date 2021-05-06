package de.mhus.lib.core.aaa;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

public class DummyRealm extends AbstractRealm {

    public DummyRealm() {
        setCredentialsMatcher(new CombiCredentialsMatcher() );
    }
    
    @Override
    public String createBearerToken(Subject subject, String issuer, BearerConfiguration configuration)
            throws ShiroException {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(String username, AuthenticationToken token) {
        if (username.equals(Aaa.USER_GUEST.value())) return Aaa.GUEST;
        if (username.equals(Aaa.USER_ADMIN.value())) return Aaa.ADMIN;
        return null;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }


}
