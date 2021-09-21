package de.mhus.lib.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.MThreadPool;
import de.mhus.lib.core.aaa.Aaa;
import de.mhus.lib.core.aaa.AccessApi;
import de.mhus.lib.core.aaa.DefaultAccessApi;
import de.mhus.lib.core.util.MDirtyTricks;
import de.mhus.lib.core.util.Value;
import de.mhus.lib.tests.TestCase;

public class ShiroMThreadTest extends TestCase {

    volatile Subject asyncSybject = null;

    @Test
    public void testAsynchron() {

        init("classpath:de/mhus/lib/test/shiro-data.ini");

        Aaa.subjectCleanup();

        // login lonestarr
        UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
        Aaa.getSubject().login(token);

        // init shiro and get subject
        System.out.println("1 Subject: " + Aaa.toString(Aaa.getSubject()));

        Value<Boolean> done = new Value<>(false);
        MThread.asynchron(new Runnable() {
            @Override
            public void run() {
                System.out.println("2 Subject: " + Aaa.toString(Aaa.getSubject()));
                asyncSybject = Aaa.getSubject();
                done.value = true;
            }
        });

        while (!done.value)
            MThread.sleep(200);
        
        System.out.println("3 Subject: " + Aaa.toString(asyncSybject));
        assertNotNull(asyncSybject);
        assertEquals("lonestarr", Aaa.toString(asyncSybject));
        
        Aaa.getSubject().logout();
    }
    
    @Test
    public void testAsynchronPool() {

        init("classpath:de/mhus/lib/test/shiro-data.ini");

        Aaa.subjectCleanup();

        // login lonestarr
        UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
        Aaa.getSubject().login(token);

        // init shiro and get subject
        System.out.println("1 Subject: " + Aaa.toString(Aaa.getSubject()));
        Value<Boolean> done = new Value<>(false);
        MThreadPool.asynchron(new Runnable() {
            @Override
            public void run() {
                System.out.println("2 Subject: " + Aaa.toString(Aaa.getSubject()));
                asyncSybject = Aaa.getSubject();
                done.value = true;
            }
        });

        while (!done.value)
            MThread.sleep(200);

        System.out.println("3 Subject: " + Aaa.toString(asyncSybject));
        assertNotNull(asyncSybject);
        assertEquals("lonestarr", Aaa.toString(asyncSybject));

        Aaa.getSubject().logout();
        Aaa.subjectCleanup();
        asyncSybject = null;

        MThreadPool.asynchron(new Runnable() {
            @Override
            public void run() {
                System.out.println("4 Subject: " + Aaa.toString(Aaa.getSubject()));
                asyncSybject = Aaa.getSubject();
            }
        });
        
        while (asyncSybject == null)
            MThread.sleep(200);
        
        System.out.println("5 Subject: " + Aaa.toString(asyncSybject));
        assertEquals("guest", Aaa.toString(asyncSybject));

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
        M.l(AccessApi.class);
    }
    
}
