/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.aaa;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.env.DefaultEnvironment;
import org.apache.shiro.env.Environment;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.subject.Subject;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.cfg.CfgString;

public class DefaultAccessApi extends MLog implements AccessApi {

    public static CfgString CFG_CONFIG_FILE =
            new CfgString(
                    AccessApi.class,
                    "iniResourcePath",
                    MApi.getFile(MApi.SCOPE.ETC, "shiro.ini").getPath());
    protected Environment env;

    public DefaultAccessApi() {
        initialize();
    }

    protected synchronized void initialize() {
        try {
            log().d("Initialize shiro", CFG_CONFIG_FILE);
            env = createEnvironment();
        } catch (Exception e) {
            log().d(e);
        }
        if (env == null || env.getSecurityManager() instanceof EmptySecurityManager) {
            HashMap<String, Object> seed = new HashMap<>();
            seed.put(DefaultEnvironment.DEFAULT_SECURITY_MANAGER_KEY, createDefaultSecurityManager() );
            env = new DefaultEnvironment(seed);
        }
        SecurityUtils.setSecurityManager(env.getSecurityManager());
    }

    private SecurityManager createDefaultSecurityManager() {
        return new DefaultSecurityManager();
    }

    protected Environment createEnvironment() {
        if (CFG_CONFIG_FILE.value().startsWith("classpath:") || new File(CFG_CONFIG_FILE.value()).exists()) {
            log().i("Initialize shiro ini", CFG_CONFIG_FILE);
            return new IniDataEnvironment(CFG_CONFIG_FILE.value());
        } else {
            log().i("Initialize shiro dummy");
            return new DummyEnvironment();
        }
    }

    @Override
    public SecurityManager getSecurityManager() {
//        return env.getSecurityManager();
        return SecurityUtils.getSecurityManager();
    }

    @Override
    public Subject createSubject() {
        return new Subject.Builder().buildSubject();
    }

    @Override
    public void updateSessionLastAccessTime() {
        Subject subject = SecurityUtils.getSubject();
        // Subject should never _ever_ be null, but just in case:
        if (subject != null) {
            Session session = subject.getSession(false);
            if (session != null) {
                try {
                    session.touch();
                } catch (Throwable t) {
                    log().e(
                                    "session.touch() method invocation has failed.  Unable to update "
                                            + "the corresponding session's last access time based on the incoming request.",
                                    t);
                }
            }
        }
    }

    public Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    @Override
    public void restart() {
        // simply restart
        initialize();
    }

    @Override
    public void destroySession() {
        try {
            Subject subject = getSubject();
            String sessionId = String.valueOf(subject.getSession().getId());
            log().d("destroySession", sessionId);
            subject.logout();
            DefaultSecurityManager securityManager =
                    (DefaultSecurityManager) SecurityUtils.getSecurityManager();

            DefaultSessionManager sessionManager =
                    (DefaultSessionManager) securityManager.getSessionManager();
            Collection<Session> activeSessions = sessionManager.getSessionDAO().getActiveSessions();
            for (Session session : activeSessions) {
                if (sessionId.equals(session.getId())) {
                    session.stop();
                }
            }
        } catch (Throwable t) {
            log().d(t);
        }
    }

}
