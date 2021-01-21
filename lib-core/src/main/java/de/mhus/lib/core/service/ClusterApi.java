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
package de.mhus.lib.core.service;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.M;
import de.mhus.lib.core.cfg.CfgBoolean;
import de.mhus.lib.core.concurrent.Lock;

@DefaultImplementation(ClusterApiDummy.class)
public interface ClusterApi {

    public static final CfgBoolean CFG_ENABLED = new CfgBoolean(ClusterApi.class, "enabled", false);

    /**
     * Get the lock object for a named resource.
     *
     * @param name
     * @return The lock for the resource
     */
    Lock getLock(String name);

    /**
     * Get a lock object for the named resource of the current service (hostname).
     *
     * @param name
     * @return The lock for the resource
     */
    default Lock getServiceLock(String name) {
        return getLock(getServiceName() + "/" + name);
    }

    /**
     * Check if I'm the master of a named resource. The method will update masters before return.
     *
     * @param name
     * @return true if I'm the master
     */
    //    boolean isMaster(String name);

    /**
     * Check if I'm the master of a named resource of the current service (hostname). The method
     * will update masters before return.
     *
     * @param name
     * @return true if I'm the master
     */
    //    default boolean isServiceMaster(String name) {
    //        return isMaster(getServiceName() + "/" + name);
    //    }

    //    void registerValueListener(String name, ValueListener consumer);

    //    default void registerServiceListener(String name, ValueListener consumer) {
    //        registerValueListener(getServiceName() + "/" + name, consumer);
    //    }

    //    void fireValueEvent(String name, String value);

    //    default void fireServiceValueEvent(String name, String value) {
    //        fireValueEvent(getServiceName() + "/" + name, value);
    //    }

    //    void unregisterValueListener(ValueListener consumer);

    default String getServiceName() {
        return M.l(ServerIdent.class).getService();
    }

    //    void registerLockListener(LockListener consumer);
    //
    //    void unregisterLockListener(LockListener consumer);

    /**
     * Return true if the cluster api is ready. The state can change multiple times while lifetime
     * and depends on active services.
     *
     * @return true if ready.
     */
    boolean isReady();
}
