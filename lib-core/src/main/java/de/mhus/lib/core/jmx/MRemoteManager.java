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
package de.mhus.lib.core.jmx;

import java.lang.management.ManagementFactory;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MHousekeeper;
import de.mhus.lib.core.MHousekeeperTask;
import de.mhus.lib.core.cfg.CfgBoolean;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.mapi.MCfgManager;
import de.mhus.lib.core.util.MObject;
import de.mhus.lib.errors.MException;

public class MRemoteManager extends MObject {

    private static CfgBoolean jmxEnabled = new CfgBoolean(MRemoteManager.class, "enabled", false);
    private MBeanServer mbs;
    private HashMap<ObjectName, Object> registry = new HashMap<ObjectName, Object>();

    private Housekeeper housekeeper;

    public MRemoteManager() throws MException {
        housekeeper = new Housekeeper(this);
        IConfig config = M.l(MCfgManager.class).getCfg(this);
        M.l(MHousekeeper.class).register(housekeeper, config.getLong("housekeeper_sleep", 30000));
    }

    public void register(JmxObject object) throws Exception {
        register(object, false);
    }

    public void register(JmxObject object, boolean weak) throws Exception {
        if (!jmxEnabled.value()) return; // disabled
        open();
        if (object instanceof JmxPackage) {
            ((JmxPackage) object).open(this);
        } else {
            ObjectName on = object.getJmxObjectName();
            //			if (registry.containsKey(on)) {
            //				log.w("object already registered, remove first",on);
            //			} else {
            register(on, object, true, weak);
            object.setJmxRegistered(true);
            //			}
        }
    }

    public void unregister(JmxObject object) throws Exception {
        if (object instanceof JmxPackage) {
            ((JmxPackage) object).close();
        } else {
            ObjectName on = object.getJmxObjectName();
            unregister(on);
            object.setJmxRegistered(false);
        }
    }

    public void register(ObjectName name, Object object, boolean capsulate, boolean weak)
            throws Exception {

        Object proxy = object;
        if (capsulate) proxy = new MBeanProxy(object, null, this, name, weak);

        synchronized (this) {
            if (mbs != null)
                try {
                    log().d("register", name);
                    mbs.registerMBean(proxy, name);
                } catch (InstanceAlreadyExistsException e) {
                    if (object instanceof JmxObject) {

                        while (true) {
                            String n = ((JmxObject) object).getJmxName();
                            int pos = n.lastIndexOf('-');
                            int nr = 1;
                            if (pos > 0) {
                                nr = MCast.toint(n.substring(pos + 1), nr) + 1;
                                n = n.substring(0, pos);
                            }
                            ((JmxObject) object).setJmxName(n + "-" + nr);
                            name = ((JmxObject) object).getJmxObjectName();
                            if (proxy instanceof MBeanProxy) ((MBeanProxy) proxy).setName(name);
                            try {
                                mbs.registerMBean(proxy, name);
                                log().t("renamed", name);
                                break;
                            } catch (InstanceAlreadyExistsException ex) {
                            }
                        }
                    } else {
                        log().d(name, e);
                        throw e;
                    }
                } catch (Exception e) {
                    log().d(name, e);
                    throw e;
                }
            registry.put(name, proxy);
        }
    }

    public void unregister(ObjectName name) {
        synchronized (this) {
            registry.remove(name);
            if (mbs != null)
                try {
                    log().t("unregister", name);
                    mbs.unregisterMBean(name);
                } catch (Exception e) {
                    log().e(name, e);
                }
        }
    }

    public void open() {
        synchronized (this) {
            if (mbs != null) return;
            mbs = ManagementFactory.getPlatformMBeanServer();
            for (Map.Entry<ObjectName, Object> item : registry.entrySet()) {
                try {
                    log().t("register", item.getKey());
                    mbs.registerMBean(item.getValue(), item.getKey());
                } catch (Exception e) {
                    log().e(item.getKey(), e);
                }
            }
        }
    }

    public void close() {
        synchronized (this) {
            if (mbs == null) return;
            for (Map.Entry<ObjectName, Object> item : registry.entrySet()) {
                try {
                    log().t("unregister", item.getKey());
                    mbs.unregisterMBean(item.getKey());
                } catch (Exception e) {
                    log().e(item.getKey(), e);
                }
            }
            mbs = null;
        }
    }

    public boolean isOpen() {
        return mbs != null;
    }

    public void check() {
        synchronized (this) {
            for (Map.Entry<ObjectName, Object> item :
                    new HashMap<ObjectName, Object>(registry).entrySet()) {
                if (item.getValue() instanceof MBeanProxy) {
                    ((MBeanProxy) item.getValue()).check();
                }
            }
        }
    }

    public MBeanServer getMBeanServer() {
        return mbs;
    }

    private static class Housekeeper extends MHousekeeperTask {

        private WeakReference<MRemoteManager> manager;

        private Housekeeper(MRemoteManager manager) {
            this.manager = new WeakReference<MRemoteManager>(manager);
        }

        @Override
        public void doit() throws Exception {
            MRemoteManager obj = manager.get();
            if (obj == null) {
                log().t("close");
                cancel();
                return;
            }
            log().t("Housekeeping");
            try {
                obj.check();
            } catch (Throwable t) {
                log().t(t);
            }
        }
    }
}
