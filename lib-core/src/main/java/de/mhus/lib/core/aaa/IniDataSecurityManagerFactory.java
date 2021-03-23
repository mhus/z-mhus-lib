package de.mhus.lib.core.aaa;

import org.apache.shiro.config.Ini;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.IniRealm;

@SuppressWarnings("deprecation")
public class IniDataSecurityManagerFactory extends IniSecurityManagerFactory {

    public IniDataSecurityManagerFactory() {
        super();
    }

    public IniDataSecurityManagerFactory(Ini config) {
        super(config);
    }

    public IniDataSecurityManagerFactory(String iniResourcePath) {
        super(iniResourcePath);
    }

    @Override
    protected Realm createRealm(Ini ini) {
        //IniRealm realm = new IniRealm(ini); changed to support SHIRO-322
        IniRealm realm = new IniDataRealm();
        realm.setName(INI_REALM_NAME);
        realm.setIni(ini); //added for SHIRO-322
        return realm;
    }

}
