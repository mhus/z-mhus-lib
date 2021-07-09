package de.mhus.lib.core.aaa;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import de.mhus.lib.core.M;
import de.mhus.lib.core.M.DEBUG;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.core.logging.Log;

public abstract class AbstractRealm extends AuthorizingRealm implements BearerRealm {

//    protected static final String[] ignoreClassList = new String[] {
//            "java.lang.",
//            "de.mhus.core"
//    }; // configurable?

    protected final Log log = Log.getLog(getClass());

    protected DEBUG debugPermissions;

    @SuppressWarnings("unused")
    private CfgString CFG_DEBUG_PERMISSIONS = new CfgString(getClass(), "debugPermissions", "yes").updateAction(v -> {
        switch (v) {
        case "trace":
        case "true":
            setDebugPermissions(DEBUG.TRACE);
            break;
        case "yes":
            setDebugPermissions(DEBUG.YES);
            break;
        default:
            setDebugPermissions(DEBUG.NO);
            break;
        }
    }).doUpdateAction();

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
                if (debugPermissions != DEBUG.NO)
                    log.i("TrustedToken access denied (3)");
                throw new AuthenticationException("TrustedToken access denied (3)");
            }
            if (debugPermissions != DEBUG.NO)
                log.i("TrustedToken access granted",Aaa.getPrincipal(),username);
            if (debugPermissions == DEBUG.TRACE)
                log.d(MSystem.currentStackTrace(null));
        }

        if (username == null)
            throw new AuthenticationException("User or Token not found");

        return doGetAuthenticationInfo(username, token);
    }

    protected abstract AuthenticationInfo doGetAuthenticationInfo(String username, AuthenticationToken token);

    protected String getUsername(PrincipalCollection principals) {
        return getAvailablePrincipal(principals).toString();
    }

    public DEBUG getDebugPermissions() {
        return debugPermissions;
    }

    public void setDebugPermissions(DEBUG debugPermissions) {
        this.debugPermissions = debugPermissions;
    }

}
