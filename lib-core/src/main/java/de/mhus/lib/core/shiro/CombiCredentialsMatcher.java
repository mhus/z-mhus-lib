package de.mhus.lib.core.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.subject.PrincipalCollection;

import de.mhus.lib.core.M;

public class CombiCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        if (token instanceof BearerToken) {
            String tokenSubject = M.l(JwsProvider.class).getSubject(((BearerToken) token).getToken());
            String infoSubject = info.getPrincipals().toString();
            return tokenSubject != null && tokenSubject.equals(infoSubject);
        }
        
        return super.doCredentialsMatch(token, info);
    }

}
