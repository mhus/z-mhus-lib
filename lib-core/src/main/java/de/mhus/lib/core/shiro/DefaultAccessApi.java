package de.mhus.lib.core.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.env.BasicIniEnvironment;
import org.apache.shiro.env.Environment;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.cfg.CfgString;

public class DefaultAccessApi extends MLog implements AccessApi {

    public static CfgString CFG_CONFIG_FILE = new CfgString(AccessApi.class,"iniResourcePath", MApi.getFile(MApi.SCOPE.ETC, "shiro.ini").getPath() );
    protected SecurityManager securityManager;
    protected Environment env;
    
    public DefaultAccessApi() {
        initialize();
    }
    
    protected void initialize() {
        log().d("Initialize Shiro",CFG_CONFIG_FILE);
        env = new BasicIniEnvironment(CFG_CONFIG_FILE.value());
        SecurityUtils.setSecurityManager(env.getSecurityManager());
//        Factory<SecurityManager> factory = new IniSecurityManagerFactory(CFG_CONFIG_FILE.value());
//        securityManager = factory.getInstance();
//        SecurityUtils.setSecurityManager(securityManager);
    }
    
    @Override
    public SecurityManager getSecurityManager() {
        return env.getSecurityManager();
    }
    
    @Override
    public Subject createSubject() {
        return new Subject.Builder().buildSubject();
    }

    @Override
    public void updateSessionLastAccessTime() {
        Subject subject = SecurityUtils.getSubject();
        //Subject should never _ever_ be null, but just in case:
        if (subject != null) {
            Session session = subject.getSession(false);
            if (session != null) {
                try {
                    session.touch();
                } catch (Throwable t) {
                    log().e("session.touch() method invocation has failed.  Unable to update " +
                            "the corresponding session's last access time based on the incoming request.", t);
                }
            }
        }
    }

    @Override
    public Subject getSubject() {
        return SecurityUtils.getSubject();
    }
    
    @Override
    public void restart() {
        // simply restart
        initialize();
    }

}
