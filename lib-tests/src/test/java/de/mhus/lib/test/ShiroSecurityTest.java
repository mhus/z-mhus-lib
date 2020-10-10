/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
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
package de.mhus.lib.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.crypt.MBouncy;
import de.mhus.lib.core.mapi.IApiInternal;
import de.mhus.lib.core.shiro.AccessApi;
import de.mhus.lib.core.shiro.AccessUtil;
import de.mhus.lib.core.shiro.BearerRealm;
import de.mhus.lib.core.shiro.DefaultAccessApi;
import de.mhus.lib.core.shiro.PrincipalData;
import de.mhus.lib.core.shiro.SubjectEnvironment;
import de.mhus.lib.core.util.MDirtyTricks;
import de.mhus.lib.test.shiro.ShiroAnnotationTest;
import de.mhus.lib.tests.TestCase;

public class ShiroSecurityTest extends TestCase {

    @Test
    public void annotationsTest() throws NoSuchMethodException, SecurityException {
        ShiroAnnotationTest test = new ShiroAnnotationTest();

        init("classpath:de/mhus/lib/test/shiro-data.ini");

        // init shiro and get subject
        System.out.println("1 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));

        try {
            AccessUtil.checkPermission(test.getClass().getMethod("permissionPrinterPrint"));
            fail();
        } catch (AuthorizationException e) {
        }

        try {
            AccessUtil.checkPermission(test.getClass().getMethod("authentication"));
            fail();
        } catch (AuthorizationException e) {
        }

        AccessUtil.checkPermission(test.getClass().getMethod("guest"));

        // admin ---------------------------------------------

        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        AccessUtil.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertTrue(AccessUtil.getSubject().isAuthenticated());
        assertEquals("admin", AccessUtil.getPrincipal());

        AccessUtil.checkPermission(test.getClass().getMethod("authentication"));

        AccessUtil.checkPermission(test.getClass().getMethod("permissionPrinterPrint"));

        try {
            AccessUtil.checkPermission(test.getClass().getMethod("guest"));
            fail();
        } catch (AuthorizationException e) {
        }

        AccessUtil.checkPermission(test.getClass().getMethod("roleAdmin"));

        // none admin -----------------------------------------

        // cleanup shiro and test new subject
        AccessUtil.subjectCleanup();
        System.out.println("3 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertFalse(AccessUtil.getSubject().isAuthenticated());

        // login lonestarr
        token = new UsernamePasswordToken("lonestarr", "vespa");
        AccessUtil.getSubject().login(token);
        // test
        System.out.println("4 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertTrue(AccessUtil.getSubject().isAuthenticated());
        assertEquals("lonestarr", AccessUtil.getPrincipal());

        AccessUtil.checkPermission(test.getClass().getMethod("authentication"));

        try {
            AccessUtil.checkPermission(test.getClass().getMethod("permissionPrinterPrint"));
            fail();
        } catch (AuthorizationException e) {
        }

        try {
            AccessUtil.checkPermission(test.getClass().getMethod("guest"));
            fail();
        } catch (AuthorizationException e) {
        }

        try {
            AccessUtil.checkPermission(test.getClass().getMethod("roleAdmin"));
            fail();
        } catch (AuthorizationException e) {
        }

        //        AccessUtil.checkPermission(test.getClass().getMethod("roleUser"));

    }

    @Test
    public void rolesTest() {
        init("classpath:de/mhus/lib/test/shiro-data.ini");

        // init shiro and get subject
        System.out.println("1 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertFalse(AccessUtil.getSubject().isAuthenticated());

        // admin ---------------------------------------------

        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        AccessUtil.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertTrue(AccessUtil.getSubject().isAuthenticated());
        assertEquals("admin", AccessUtil.getPrincipal());

        assertTrue(AccessUtil.hasRole("admin"));
        assertFalse(AccessUtil.hasRole("user")); // should have all roles!!

        // none admin -----------------------------------------

        // cleanup shiro and test new subject
        AccessUtil.subjectCleanup();
        System.out.println("3 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertFalse(AccessUtil.getSubject().isAuthenticated());

        // login lonestarr
        token = new UsernamePasswordToken("lonestarr", "vespa");
        AccessUtil.getSubject().login(token);
        // test
        System.out.println("4 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertTrue(AccessUtil.getSubject().isAuthenticated());
        assertEquals("lonestarr", AccessUtil.getPrincipal());

        assertFalse(AccessUtil.hasRole("admin"));
    }

    @Test
    public void permissionsTest() {
        init("classpath:de/mhus/lib/test/shiro-data.ini");

        // init shiro and get subject
        System.out.println("1 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertFalse(AccessUtil.getSubject().isAuthenticated());

        // admin ---------------------------------------------

        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        AccessUtil.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertTrue(AccessUtil.getSubject().isAuthenticated());
        assertEquals("admin", AccessUtil.getPrincipal());

        assertTrue(AccessUtil.isPermitted("printer", "print", null));

        // none admin -----------------------------------------

        // cleanup shiro and test new subject
        AccessUtil.subjectCleanup();
        System.out.println("3 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertFalse(AccessUtil.getSubject().isAuthenticated());

        // login lonestarr
        token = new UsernamePasswordToken("lonestarr", "vespa");
        AccessUtil.getSubject().login(token);
        // test
        System.out.println("4 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertTrue(AccessUtil.getSubject().isAuthenticated());
        assertEquals("lonestarr", AccessUtil.getPrincipal());

        assertFalse(AccessUtil.isPermitted("printer", "print", null));
    }

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
        // cleanup shiro and test new subject
        AccessUtil.subjectCleanup();
        System.out.println("3 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        System.out.println("3 Session: " + AccessUtil.getSessionId(false));
        assertFalse(AccessUtil.getSubject().isAuthenticated());

        // recreate session
        Subject session = AccessUtil.createSubjectFromSessionId(sessionId);
        session.execute(
                () -> {
                    System.out.println(
                            "4 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
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

        // cleanup shiro and test new subject
        AccessUtil.subjectCleanup();
        System.out.println("3 Subject: " + AccessUtil.toString(AccessUtil.getSubject()));
        assertFalse(AccessUtil.getSubject().isAuthenticated());

        // login
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
            System.out.println("ID: " + session.getId());
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
    
    @Test
    public void testJWToken() {
        ((IApiInternal)MApi.get()).setBaseDir(new File("target"));
        MFile.deleteDir(new File("target/keys")); // remove old if exists
        init("classpath:de/mhus/lib/test/shiro-data.ini");
        MBouncy.init();

//        Provider provider = Security.getProvider("BC");
//        for (java.security.Provider.Service service : provider.getServices()) {
//            System.out.println(service.getType() + ": " + service.getAlgorithm());
//        }
        
        assertFalse(AccessUtil.getSubject().isAuthenticated());
        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        AccessUtil.getSubject().login(token);
        assertTrue(AccessUtil.getSubject().isAuthenticated());
        
        // create token
        String jwt = null;
        for (Realm realm : AccessUtil.getRealms()) {
            if (realm instanceof BearerRealm) {
                jwt = ((BearerRealm)realm).createBearerToken(AccessUtil.getSubject());
                System.out.println(jwt);

            }
        }
        assertNotNull(jwt);
        
        // logout
        AccessUtil.getSubject().logout();
        assertFalse(AccessUtil.getSubject().isAuthenticated());
        
        // login with token
        BearerToken jwtToken = new BearerToken(jwt);
        AccessUtil.getSubject().login(jwtToken);
        assertTrue(AccessUtil.getSubject().isAuthenticated());
        
        AccessUtil.getSubject().logout();
        
        
    }

    private void init(String config) {
        System.out.println();
        System.out.println(">>> " + MSystem.findCallingMethod(3));
        // --- Prepare
        MDirtyTricks.setTestLogging();
        // cleanup shiro
        MApi.get().getLookupActivator().removeObject(AccessApi.class, null);
        AccessUtil.subjectCleanup();
        // touch class
        DefaultAccessApi.CFG_CONFIG_FILE.value();
        // patch value
        assertTrue(MDirtyTricks.updateCfgValue(AccessApi.class, "iniResourcePath", config));
    }
}
