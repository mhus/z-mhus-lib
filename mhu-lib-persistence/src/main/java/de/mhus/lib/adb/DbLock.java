package de.mhus.lib.adb;

import de.mhus.lib.basics.MCloseable;

public class DbLock implements MCloseable {

    public DbLock(Persistable ... objects) {
        // TODO
    }
    
    @Override
    public void close() {
        DbTransaction.releaseLock();
    }

}
