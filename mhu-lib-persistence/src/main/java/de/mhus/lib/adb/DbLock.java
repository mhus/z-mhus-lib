package de.mhus.lib.adb;

import java.io.Closeable;
import java.io.IOException;

public class DbLock implements Closeable {

    public DbLock(Persistable ... objects) {
        // TODO
    }
    
    @Override
    public void close() throws IOException {
        DbTransaction.releaseLock();
    }

}
