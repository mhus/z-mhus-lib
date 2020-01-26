/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.jmx;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MSystem;

public class MJmx extends JmxObject {

    public MJmx() {
        this(true, MSystem.findCalling());
    }

    public MJmx(boolean weak, String name) {
        jmxRegister(weak, name);
    }

    protected void jmxRegister(boolean weak, String name) {
        if (!isJmxRegistered()) {
            try {
                setJmxName(name);
                M.l(MRemoteManager.class).register(this, weak);
            } catch (Throwable e) {
                log().t(e);
            }
        }
    }
}
