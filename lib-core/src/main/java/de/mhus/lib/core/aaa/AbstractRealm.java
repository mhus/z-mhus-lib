package de.mhus.lib.core.aaa;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import de.mhus.lib.core.M;
import de.mhus.lib.core.logging.Log;

public abstract class AbstractRealm extends AuthorizingRealm implements BearerRealm {

//    protected static final String[] ignoreClassList = new String[] {
//            "java.lang.",
//            "de.mhus.core"
//    }; // configurable?

    protected final Log log = Log.getLog(getClass());

    protected boolean debugPermissions;

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token != null && 
                (
                        token instanceof TrustedToken
                        ||
                        token instanceof BearerToken
                    )) return true;
        return super.supports(token);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {

        String username = null;
        if (token instanceof UsernamePasswordToken) {
            UsernamePasswordToken upToken = (UsernamePasswordToken) token;
            username = upToken.getUsername();
        } else if (token instanceof BearerToken) {
            String tokenStr = ((BearerToken) token).getToken();
            JwsData jwtToken = M.l(JwtProvider.class).readToken(tokenStr);
            username = jwtToken.getSubject();
        } else if (token instanceof TrustedToken) {
            username = (String)((TrustedToken)token).getPrincipal();

            if (username.equals(Aaa.USER_GUEST.value())) return Aaa.GUEST;
            // check permissions to use trusted token

            boolean access = ((TrustedToken)token).hasAccess(debugPermissions);

            if (!access) {
                if (debugPermissions)
                    log.i("TrustedToken access denied (3)");
                throw new AuthenticationException("TrustedToken access denied (3)");
            }
            if (debugPermissions)
                log.i("TrustedToken access granted",Aaa.getPrincipal(),username);

        }

        if (username == null)
            throw new AuthenticationException("User or Token not found");

        return doGetAuthenticationInfo(username, token);
    }

    protected abstract AuthenticationInfo doGetAuthenticationInfo(String username, AuthenticationToken token);

    protected String getUsername(PrincipalCollection principals) {
        return getAvailablePrincipal(principals).toString();
    }

    public boolean isDebugPermissions() {
        return debugPermissions;
    }

    public void setDebugPermissions(boolean debugPermissions) {
        this.debugPermissions = debugPermissions;
    }

}
