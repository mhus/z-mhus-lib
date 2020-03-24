package de.mhus.lib.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.base.SingleBaseStrategy;
import de.mhus.lib.core.lang.MDirtyTricks;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.shiro.DefaultAccessApi;
import de.mhus.lib.core.shiro.PrincipalData;
import de.mhus.lib.core.shiro.AccessApi;
import de.mhus.lib.core.shiro.AccessUtil;
import de.mhus.lib.core.shiro.SubjectEnvironment;

public class ShiroSecurityTest {

    @Test
    public void sessionTest() {
        init("classpath:de/mhus/lib/test/shiro-simple.ini");
        
        // init shiro and get subject
        System.out.println("1 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertFalse(AccessUtil.getSubject().isAuthenticated());
        
        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        AccessUtil.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertTrue(AccessUtil.getSubject().isAuthenticated());
        assertEquals("admin", AccessUtil.getPrincipal());
        
        String sessionId = AccessUtil.getSessionId(true);
        System.out.println("2 Session: " + AccessUtil.getSessionId(false));
        //cleanup shiro and test new subject
        AccessUtil.subjectCleanup();
        System.out.println("3 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        System.out.println("3 Session: " + AccessUtil.getSessionId(false));
        assertFalse(AccessUtil.getSubject().isAuthenticated());

        // recreate session
        Subject session = AccessUtil.createSubjectFromSessionId(sessionId);
        session.execute(() -> {
            System.out.println("4 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
            System.out.println("4 Session: " + AccessUtil.getSessionId(false));
            assertTrue(AccessUtil.getSubject().isAuthenticated());
            assertEquals("admin", AccessUtil.getPrincipal());
        });
        
        
    }
    
    @Test
    public void subjectBinding() {
        init("classpath:de/mhus/lib/test/shiro-simple.ini");
        
        // init shiro and get subject
        Subject subject = AccessUtil.getSubject();
        System.out.println("1 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertFalse(AccessUtil.getSubject().isAuthenticated());
        
        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        AccessUtil.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertTrue(AccessUtil.getSubject().isAuthenticated());
        assertEquals("admin", AccessUtil.getPrincipal());

        //cleanup shiro and test new subject
        AccessUtil.subjectCleanup();
        System.out.println("3 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertFalse(AccessUtil.getSubject().isAuthenticated());
        
        //login
        token = new UsernamePasswordToken("lonestarr", "vespa");
        AccessUtil.getSubject().login(token);
        // test
        System.out.println("4 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertTrue(AccessUtil.getSubject().isAuthenticated());
        assertEquals("lonestarr", AccessUtil.getPrincipal());
        
        // use a different subject for a while
        try (SubjectEnvironment env = AccessUtil.useSubject(subject)) {
            System.out.println("5 Env: " + env);
            System.out.println("5 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
            assertTrue(AccessUtil.getSubject().isAuthenticated());
            assertEquals("admin", AccessUtil.getPrincipal());
        }
        
        // check if subject was restored
        System.out.println("6 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertTrue(AccessUtil.getSubject().isAuthenticated());
        assertEquals("lonestarr", AccessUtil.getPrincipal());
        
    }

    @Test
    public void testRealms() {
        init("classpath:de/mhus/lib/test/shiro-simple.ini");
        
        // init shiro and get subject
        Subject subject = AccessUtil.getSubject();
        System.out.println("1 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertFalse(AccessUtil.getSubject().isAuthenticated());

        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        AccessUtil.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertTrue(AccessUtil.getSubject().isAuthenticated());
        assertEquals("admin", AccessUtil.getPrincipal());

        for (Realm realm : AccessUtil.getRealms()) {
            System.out.println(realm);
        }
        
        Session session = subject.getSession(false);
        System.out.println(session);
        if (session != null) {
            System.out.println("ID: " + session.getId() );
            System.out.println("Host: " + session.getHost());

            for (Object key : session.getAttributeKeys()) {
                System.out.println(key + "=" + session.getAttribute(key));
            }
        }
        
        AccessUtil.getPrincipalData();
        
    }
    

    @Test
    public void testDataRealms() {
        init("classpath:de/mhus/lib/test/shiro-data.ini");

        // init shiro and get subject
        System.out.println("1 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertFalse(AccessUtil.getSubject().isAuthenticated());

        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        AccessUtil.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertTrue(AccessUtil.getSubject().isAuthenticated());
        assertEquals("admin", AccessUtil.getPrincipal());

        PrincipalData data = AccessUtil.getPrincipalData();
        System.out.println("DisplayName: " + data.getDisplayName());

        assertEquals("Administrator", data.getDisplayName());
    }

    private void init(String config) {
        System.out.println();
        System.out.println(">>> " + MSystem.findCallingMethod(3));
        // --- Prepare
        MApi.setDirtyTrace(true);
        MApi.get().getLogFactory().setDefaultLevel(Log.LEVEL.DEBUG);
        MApi.get().getBaseControl().setFindStrategy(new SingleBaseStrategy());
        //cleanup shiro
        AccessUtil.subjectCleanup();
       // touch class
        DefaultAccessApi.CFG_CONFIG_FILE.value();
        // patch value
        assertTrue(MDirtyTricks.updateCfgValue(AccessApi.class, "iniResourcePath", config));
    }
    
}
