package de.mhus.lib.core.concurrent;

import de.mhus.lib.core.M;
import de.mhus.lib.core.base.service.LockManager;

public class LocalLock implements Lock {

    protected Thread lock = null;
    protected String name;
    protected boolean privacy = false;
    protected long lockTime = 0;

    public LocalLock() {
    }

    public LocalLock(String name, boolean privacy) {
        this(name);
        this.privacy = privacy;
    }

    public LocalLock(String name) {
        setName(name);
        register();
    }

    protected void register() {
        M.l(LockManager.class).register(this);
    }

    @Override
    public Lock lock() {
        synchronized (this) {

            while (isLocked()) {
                try {
                    wait();
                } catch (InterruptedException e) {

                }
            }
            lock = Thread.currentThread();
            lockTime = System.currentTimeMillis();
            lockEvent(true);
        }
        return this;
    }

    /**
     * Overwrite this to get the lock events.
     * 
     * @param locked
     */
    protected void lockEvent(boolean locked) {
    
    }

      @Override
    public boolean lock(long timeout) {
          synchronized (this) {
            
            while(isLocked()){
                long start = System.currentTimeMillis();
              try {
                wait(timeout);
            } catch (InterruptedException e) {
            }
              if (System.currentTimeMillis() - start >= timeout ) return false;
            }
            lock = Thread.currentThread();
            lockTime = System.currentTimeMillis();
            lockEvent(true);
            return true;
          }
      }
    
      /**
       * Unlock if the current thread is also the owner.
       * 
       * @return true if unlock was successful
       */
      @Override
    public boolean unlock(){
          synchronized (this) {
            if (lock != Thread.currentThread()) return false;
            lockEvent(false);
            lock = null;
            lockTime = 0;
            notify();
            return true;
          }
      }

      /**
       * Unlock in every case !!! This can break a locked area.
       * 
       */
      @Override
    public void unlockHard(){
          synchronized (this) {
            lockEvent(false);
            lock = null;
            lockTime = 0;
            notify();
          }
      }
      
    @Override
    public boolean isLocked() {
        return lock != null;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public Thread getLocker() {
        if (privacy) return null;
        return lock;
    }
    
    @Override
    public boolean isPrivacy() {
        return privacy;
    }
    
    @Override
    public String toString() {
        return name + (lock != null ? " " + lock.getName() : "");
    }
      
    @Override
    public long getLockTime() {
        return lockTime;
    }

}
