package de.mhus.lib.core.concurrent;

public interface LockWithExtend extends Lock {

    /**
     * How long to extend after unlock.
     * 
     * @param extend The time in ms to extend after unlock.
     * @return True if unlock was successful
     */
    boolean unlock(long extend);

}
