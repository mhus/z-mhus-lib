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

import de.mhus.lib.core.M;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.aaa.Aaa;
import de.mhus.lib.core.aaa.AccessApi;
import de.mhus.lib.core.aaa.BearerConfiguration;
import de.mhus.lib.core.aaa.DefaultAccessApi;
import de.mhus.lib.core.aaa.JwtProvider;
import de.mhus.lib.core.aaa.JwtProviderImpl;
import de.mhus.lib.core.aaa.PrincipalData;
import de.mhus.lib.core.aaa.SubjectEnvironment;
import de.mhus.lib.core.mapi.IApiInternal;
import de.mhus.lib.core.util.MDirtyTricks;
import de.mhus.lib.test.shiro.ShiroAnnotationTest;
import de.mhus.lib.tests.TestCase;

public class ShiroSecurityTest extends TestCase {
    
//    @Test
//    public void envTest() throws NoSuchMethodException, SecurityException {
//
//        init("classpath:de/mhus/lib/test/shiro-data.ini");
//        SecurityManager manager = SecurityUtils.getSecurityManager();
//        AccessApi api = M.l(AccessApi.class);
//    }

    @Test
    public void annotationsTest() throws NoSuchMethodException, SecurityException {
        ShiroAnnotationTest test = new ShiroAnnotationTest();

        init("classpath:de/mhus/lib/test/shiro-data.ini");

        // init shiro and get subject
        System.out.println("1 Subject: " + Aaa.toString(Aaa.getSubject()));

        try {
            Aaa.checkPermission(test.getClass().getMethod("permissionPrinterPrint"));
            fail();
        } catch (AuthorizationException e) {
        }

        try {
            Aaa.checkPermission(test.getClass().getMethod("authentication"));
            fail();
        } catch (AuthorizationException e) {
        }

        Aaa.checkPermission(test.getClass().getMethod("guest"));

        // admin ---------------------------------------------

        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        Aaa.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertTrue(Aaa.getSubject().isAuthenticated());
        assertEquals("admin", Aaa.getPrincipal());

        Aaa.checkPermission(test.getClass().getMethod("authentication"));

        Aaa.checkPermission(test.getClass().getMethod("permissionPrinterPrint"));

        try {
            Aaa.checkPermission(test.getClass().getMethod("guest"));
            fail();
        } catch (AuthorizationException e) {
        }

        Aaa.checkPermission(test.getClass().getMethod("roleAdmin"));

        // none admin -----------------------------------------

        // cleanup shiro and test new subject
        Aaa.subjectCleanup();
        System.out.println("3 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertFalse(Aaa.getSubject().isAuthenticated());

        // login lonestarr
        token = new UsernamePasswordToken("lonestarr", "vespa");
        Aaa.getSubject().login(token);
        // test
        System.out.println("4 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertTrue(Aaa.getSubject().isAuthenticated());
        assertEquals("lonestarr", Aaa.getPrincipal());

        Aaa.checkPermission(test.getClass().getMethod("authentication"));

        try {
            Aaa.checkPermission(test.getClass().getMethod("permissionPrinterPrint"));
            fail();
        } catch (AuthorizationException e) {
        }

        try {
            Aaa.checkPermission(test.getClass().getMethod("guest"));
            fail();
        } catch (AuthorizationException e) {
        }

        try {
            Aaa.checkPermission(test.getClass().getMethod("roleAdmin"));
            fail();
        } catch (AuthorizationException e) {
        }

        //        AccessUtil.checkPermission(test.getClass().getMethod("roleUser"));

    }

    @Test
    public void rolesTest() {
        init("classpath:de/mhus/lib/test/shiro-data.ini");

        // init shiro and get subject
        System.out.println("1 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertFalse(Aaa.getSubject().isAuthenticated());

        // admin ---------------------------------------------

        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        Aaa.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertTrue(Aaa.getSubject().isAuthenticated());
        assertEquals("admin", Aaa.getPrincipal());

        assertTrue(Aaa.hasRole("admin"));
        assertFalse(Aaa.hasRole("user")); // should have all roles!!

        // none admin -----------------------------------------

        // cleanup shiro and test new subject
        Aaa.subjectCleanup();
        System.out.println("3 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertFalse(Aaa.getSubject().isAuthenticated());

        // login lonestarr
        token = new UsernamePasswordToken("lonestarr", "vespa");
        Aaa.getSubject().login(token);
        // test
        System.out.println("4 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertTrue(Aaa.getSubject().isAuthenticated());
        assertEquals("lonestarr", Aaa.getPrincipal());

        assertFalse(Aaa.hasRole("admin"));
    }

    @Test
    public void permissionsTest() {
        init("classpath:de/mhus/lib/test/shiro-data.ini");

        // init shiro and get subject
        System.out.println("1 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertFalse(Aaa.getSubject().isAuthenticated());

        // admin ---------------------------------------------

        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        Aaa.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertTrue(Aaa.getSubject().isAuthenticated());
        assertEquals("admin", Aaa.getPrincipal());

        assertTrue(Aaa.isPermitted("printer", "print", null));

        // none admin -----------------------------------------

        // cleanup shiro and test new subject
        Aaa.subjectCleanup();
        System.out.println("3 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertFalse(Aaa.getSubject().isAuthenticated());

        // login lonestarr
        token = new UsernamePasswordToken("lonestarr", "vespa");
        Aaa.getSubject().login(token);
        // test
        System.out.println("4 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertTrue(Aaa.getSubject().isAuthenticated());
        assertEquals("lonestarr", Aaa.getPrincipal());

        assertFalse(Aaa.isPermitted("printer", "print", null));
    }

    @Test
    public void sessionTest() {
        init("classpath:de/mhus/lib/test/shiro-simple.ini");

        // init shiro and get subject
        System.out.println("1 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertFalse(Aaa.getSubject().isAuthenticated());

        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        Aaa.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertTrue(Aaa.getSubject().isAuthenticated());
        assertEquals("admin", Aaa.getPrincipal());

        String sessionId = Aaa.getSessionId(true);
        System.out.println("2 Session: " + Aaa.getSessionId(false));
        // cleanup shiro and test new subject
        Aaa.subjectCleanup();
        System.out.println("3 Subject: " + Aaa.toString(Aaa.getSubject()));
        System.out.println("3 Session: " + Aaa.getSessionId(false));
        assertFalse(Aaa.getSubject().isAuthenticated());

        // recreate session
        Subject session = Aaa.createSubjectFromSessionId(sessionId);
        session.execute(
                () -> {
                    System.out.println(
                            "4 Subject: " + Aaa.toString(Aaa.getSubject()));
                    System.out.println("4 Session: " + Aaa.getSessionId(false));
                    assertTrue(Aaa.getSubject().isAuthenticated());
                    assertEquals("admin", Aaa.getPrincipal());
                });
    }

    @Test
    public void subjectBinding() {
        init("classpath:de/mhus/lib/test/shiro-simple.ini");

        // init shiro and get subject
        Subject subject = Aaa.getSubject();
        System.out.println("1 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertFalse(Aaa.getSubject().isAuthenticated());

        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        Aaa.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertTrue(Aaa.getSubject().isAuthenticated());
        assertEquals("admin", Aaa.getPrincipal());

        // cleanup shiro and test new subject
        Aaa.subjectCleanup();
        System.out.println("3 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertFalse(Aaa.getSubject().isAuthenticated());

        // login
        token = new UsernamePasswordToken("lonestarr", "vespa");
        Aaa.getSubject().login(token);
        // test
        System.out.println("4 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertTrue(Aaa.getSubject().isAuthenticated());
        assertEquals("lonestarr", Aaa.getPrincipal());

        // use a different subject for a while
        try (SubjectEnvironment env = Aaa.asSubject(subject)) {
            System.out.println("5 Env: " + env);
            System.out.println("5 Subject: " + Aaa.toString(Aaa.getSubject()));
            assertTrue(Aaa.getSubject().isAuthenticated());
            assertEquals("admin", Aaa.getPrincipal());
        }

        // check if subject was restored
        System.out.println("6 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertTrue(Aaa.getSubject().isAuthenticated());
        assertEquals("lonestarr", Aaa.getPrincipal());
    }

    @Test
    public void testRealms() {
        init("classpath:de/mhus/lib/test/shiro-simple.ini");

        // init shiro and get subject
        Subject subject = Aaa.getSubject();
        System.out.println("1 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertFalse(Aaa.getSubject().isAuthenticated());

        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        Aaa.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertTrue(Aaa.getSubject().isAuthenticated());
        assertEquals("admin", Aaa.getPrincipal());

        for (Realm realm : Aaa.getRealms()) {
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

        Aaa.getPrincipalData();
    }

    @Test
    public void testDataRealms() {
        init("classpath:de/mhus/lib/test/shiro-data.ini");

        // init shiro and get subject
        System.out.println("1 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertFalse(Aaa.getSubject().isAuthenticated());

        // login
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
        Aaa.getSubject().login(token);
        // test
        System.out.println("2 Subject: " + Aaa.toString(Aaa.getSubject()));
        assertTrue(Aaa.getSubject().isAuthenticated());
        assertEquals("admin", Aaa.getPrincipal());

        PrincipalData data = Aaa.getPrincipalData();
        System.out.println("DisplayName: " + data.getDisplayName());

        assertEquals("Administrator", data.getDisplayName());
    }

    @Test
    public void testJWToken() {
        ((IApiInternal) MApi.get()).setBaseDir(new File("target"));
        MFile.deleteDir(
                new File(
                        "target/de.mhus.lib.core.vault.FolderVaultSource")); // remove old if exists
        init("classpath:de/mhus/lib/test/shiro-data.ini");
        //        MBouncy.init();

        //        Provider provider = Security.getProvider("BC");
        //        for (java.security.Provider.Service service : provider.getServices()) {
        //            System.out.println(service.getType() + ": " + service.getAlgorithm());
        //        }
        {
            assertFalse(Aaa.getSubject().isAuthenticated());
            // login
            UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
            Aaa.getSubject().login(token);
            assertTrue(Aaa.getSubject().isAuthenticated());

            // create token
            String jwt = Aaa.createBearerToken(Aaa.getSubject(), null);
            System.out.println(jwt);
            assertNotNull(jwt);

            // logout
            Aaa.getSubject().logout();
            assertFalse(Aaa.getSubject().isAuthenticated());

            // login with token
            BearerToken jwtToken = new BearerToken(jwt);
            Aaa.getSubject().login(jwtToken);
            assertTrue(Aaa.getSubject().isAuthenticated());

            Aaa.getSubject().logout();
        }
        // -----------
        // reset JwtProvider internal and try again with loading from keychain
        ((JwtProviderImpl) M.l(JwtProvider.class)).clear();
        {
            assertFalse(Aaa.getSubject().isAuthenticated());
            // login
            UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
            Aaa.getSubject().login(token);
            assertTrue(Aaa.getSubject().isAuthenticated());

            // create token
            String jwt = Aaa.createBearerToken(Aaa.getSubject(), null);
            System.out.println(jwt);
            assertNotNull(jwt);

            // logout
            Aaa.getSubject().logout();
            assertFalse(Aaa.getSubject().isAuthenticated());

            // login with token
            BearerToken jwtToken = new BearerToken(jwt);
            Aaa.getSubject().login(jwtToken);
            assertTrue(Aaa.getSubject().isAuthenticated());

            Aaa.getSubject().logout();
        }

        // ---------------
        // test timeout
        {
            assertFalse(Aaa.getSubject().isAuthenticated());
            // login
            UsernamePasswordToken token = new UsernamePasswordToken("admin", "secret");
            Aaa.getSubject().login(token);
            assertTrue(Aaa.getSubject().isAuthenticated());

            // create token
            BearerConfiguration config = new BearerConfiguration(1);
            String jwt = Aaa.createBearerToken(Aaa.getSubject(), null, config);
            System.out.println(jwt);
            assertNotNull(jwt);

            // logout
            Aaa.getSubject().logout();
            assertFalse(Aaa.getSubject().isAuthenticated());

            MThread.sleep(500);

            // login with token
            BearerToken jwtToken = new BearerToken(jwt);
            try {
                Aaa.getSubject().login(jwtToken);
                fail();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            assertFalse(Aaa.getSubject().isAuthenticated());
            Aaa.getSubject().logout();
        }
    }

    private void init(String config) {
        System.out.println();
        System.out.println(">>> " + MSystem.findCallingMethod(3));
        // --- Prepare
        MDirtyTricks.setTestLogging();
        // cleanup shiro
        MApi.get().getLookupActivator().removeObject(AccessApi.class, null);
        Aaa.subjectCleanup();
        // touch class
        DefaultAccessApi.CFG_CONFIG_FILE.value();
        // patch values
        assertTrue(MDirtyTricks.updateCfgValue(AccessApi.class, "iniResourcePath", config));
        assertTrue(MDirtyTricks.updateCfgValue(AccessApi.class, "allowAdminLogin", "true"));
    }
}
