package de.mhus.lib.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.base.SingleBaseStrategy;
import de.mhus.lib.core.lang.MDirtyTricks;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.shiro.DefaultShiroSecurity;
import de.mhus.lib.core.shiro.ShiroSecurity;
import de.mhus.lib.core.shiro.ShiroUtil;
import de.mhus.lib.core.shiro.SubjectEnvironment;

public class ShiroSecurityTest {

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

        for (Realm realm : ShiroUtil.getReals()) {
            System.out.println(realm);
        }
        
    }
    

    
    
    private void init(String config) {
        System.out.println();
        System.out.println(">>> " + MSystem.findCallingMethod(3));
        // --- Prepare
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
