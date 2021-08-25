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

import org.apache.shiro.env.DefaultEnvironment;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MCast;

public class DummyEnvironment extends DefaultEnvironment {

    public DummyEnvironment() {
        DefaultSecurityManager manager =
                (DefaultSecurityManager) new IniDataSecurityManagerFactory().getInstance();
        manager.setRealm(new DummyRealm());
        setSecurityManager(manager);
        long globalSessionTimeout =
                MCast.tolong(
                        MApi.get().getCfgString(Aaa.class, "globalSessionTimeout", null),
                        3600000); // 1h
        ((DefaultSessionManager) manager.getSessionManager())
                .setGlobalSessionTimeout(globalSessionTimeout);
    }
}
