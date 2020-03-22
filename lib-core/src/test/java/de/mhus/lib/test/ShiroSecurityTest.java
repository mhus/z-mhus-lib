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
import de.mhus.lib.core.shiro.DefaultShiroSecurity;
import de.mhus.lib.core.shiro.PrincipalData;
import de.mhus.lib.core.shiro.ShiroSecurity;
import de.mhus.lib.core.shiro.ShiroUtil;
import de.mhus.lib.core.shiro.SubjectEnvironment;

public class ShiroSecurityTest {

    @Test
    public void sessionTest() {
        init("classpath:de/mhus/lib/test/shiro-simple.ini");
        
        // init shiro and get subject
        System.out.println("1 Subject: " + ShiroUtil.toString(ShiroUtil.getSubject()));
        assertFalse(ShiroUtil.getSubject().isAuthenticated());
        
        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        ShiroUtil.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + ShiroUtil.toString(ShiroUtil.getSubject()));
        assertTrue(ShiroUtil.getSubject().isAuthenticated());
        assertEquals("admin", ShiroUtil.getPrincipal());
        
        String sessionId = ShiroUtil.getSessionId(true);
        System.out.println("2 Session: " + ShiroUtil.getSessionId(false));
        //cleanup shiro and test new subject
        ShiroUtil.subjectCleanup();
        System.out.println("3 Subject: " + ShiroUtil.toString(ShiroUtil.getSubject()));
        System.out.println("3 Session: " + ShiroUtil.getSessionId(false));
        assertFalse(ShiroUtil.getSubject().isAuthenticated());

        // recreate session
        Subject session = ShiroUtil.createSubjectFromSessionId(sessionId);
        session.execute(() -> {
            System.out.println("4 Subject: " + ShiroUtil.toString(ShiroUtil.getSubject()));
            System.out.println("4 Session: " + ShiroUtil.getSessionId(false));
            assertTrue(ShiroUtil.getSubject().isAuthenticated());
            assertEquals("admin", ShiroUtil.getPrincipal());
        });
        
        
    }
    
    @Test
    public void subjectBinding() {
        init("classpath:de/mhus/lib/test/shiro-simple.ini");
        
        // init shiro and get subject
        Subject subject = ShiroUtil.getSubject();
        System.out.println("1 Subject: " + ShiroUtil.toString(ShiroUtil.getSubject()));
        assertFalse(ShiroUtil.getSubject().isAuthenticated());
        
        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        ShiroUtil.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + ShiroUtil.toString(ShiroUtil.getSubject()));
        assertTrue(ShiroUtil.getSubject().isAuthenticated());
        assertEquals("admin", ShiroUtil.getPrincipal());

        //cleanup shiro and test new subject
        ShiroUtil.subjectCleanup();
        System.out.println("3 Subject: " + ShiroUtil.toString(ShiroUtil.getSubject()));
        assertFalse(ShiroUtil.getSubject().isAuthenticated());
        
        //login
        token = new UsernamePasswordToken("lonestarr", "vespa");
        ShiroUtil.getSubject().login(token);
        // test
        System.out.println("4 Subject: " + ShiroUtil.toString(ShiroUtil.getSubject()));
        assertTrue(ShiroUtil.getSubject().isAuthenticated());
        assertEquals("lonestarr", ShiroUtil.getPrincipal());
        
        // use a different subject for a while
        try (SubjectEnvironment env = ShiroUtil.useSubject(subject)) {
            System.out.println("5 Env: " + env);
            System.out.println("5 Subject: " + ShiroUtil.toString(ShiroUtil.getSubject()));
            assertTrue(ShiroUtil.getSubject().isAuthenticated());
            assertEquals("admin", ShiroUtil.getPrincipal());
        }
        
        // check if subject was restored
        System.out.println("6 Subject: " + ShiroUtil.toString(ShiroUtil.getSubject()));
        assertTrue(ShiroUtil.getSubject().isAuthenticated());
        assertEquals("lonestarr", ShiroUtil.getPrincipal());
        
    }

    @Test
    public void testRealms() {
        init("classpath:de/mhus/lib/test/shiro-simple.ini");
        
        // init shiro and get subject
        Subject subject = ShiroUtil.getSubject();
        System.out.println("1 Subject: " + ShiroUtil.toString(ShiroUtil.getSubject()));
        assertFalse(ShiroUtil.getSubject().isAuthenticated());

        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        ShiroUtil.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + ShiroUtil.toString(ShiroUtil.getSubject()));
        assertTrue(ShiroUtil.getSubject().isAuthenticated());
        assertEquals("admin", ShiroUtil.getPrincipal());

        for (Realm realm : ShiroUtil.getRealms()) {
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
        
        ShiroUtil.getPrincipalData();
        
    }
    

    @Test
    public void testDataRealms() {
        init("classpath:de/mhus/lib/test/shiro-data.ini");

        // init shiro and get subject
        System.out.println("1 Subject: " + ShiroUtil.toString(ShiroUtil.getSubject()));
        assertFalse(ShiroUtil.getSubject().isAuthenticated());

        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        ShiroUtil.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + ShiroUtil.toString(ShiroUtil.getSubject()));
        assertTrue(ShiroUtil.getSubject().isAuthenticated());
        assertEquals("admin", ShiroUtil.getPrincipal());

        PrincipalData data = ShiroUtil.getPrincipalData();
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
        ShiroUtil.subjectCleanup();
       // touch class
        DefaultShiroSecurity.CFG_CONFIG_FILE.value();
        // patch value
        assertTrue(MDirtyTricks.updateCfgValue(ShiroSecurity.class, "iniResourcePath", config));
    }
    
}
