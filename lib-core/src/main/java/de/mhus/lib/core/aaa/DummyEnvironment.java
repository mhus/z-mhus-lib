package de.mhus.lib.core.aaa;

import org.apache.shiro.env.DefaultEnvironment;
import org.apache.shiro.mgt.DefaultSecurityManager;

public class DummyEnvironment extends DefaultEnvironment {

    public DummyEnvironment() {
        DefaultSecurityManager manager = (DefaultSecurityManager) new IniDataSecurityManagerFactory().getInstance();
        manager.setRealm(new DummyRealm());
        setSecurityManager(manager);
    }

}
