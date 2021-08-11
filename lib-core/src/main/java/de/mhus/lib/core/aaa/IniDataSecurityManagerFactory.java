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
        // IniRealm realm = new IniRealm(ini); changed to support SHIRO-322
        IniRealm realm = new IniDataRealm();
        realm.setName(INI_REALM_NAME);
        realm.setIni(ini); // added for SHIRO-322
        return realm;
    }
}
