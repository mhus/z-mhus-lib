package de.mhus.lib.core.aaa;

import org.apache.shiro.config.Ini;
import org.apache.shiro.env.DefaultEnvironment;

public class IniDataEnvironment extends DefaultEnvironment {

    public IniDataEnvironment(Ini ini) {
        setSecurityManager(new IniDataSecurityManagerFactory(ini).getInstance());
    }

    public IniDataEnvironment(String iniResourcePath) {
        this(Ini.fromResourcePath(iniResourcePath));
    }

}
