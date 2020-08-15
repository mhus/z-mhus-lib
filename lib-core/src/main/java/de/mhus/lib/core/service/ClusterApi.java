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
