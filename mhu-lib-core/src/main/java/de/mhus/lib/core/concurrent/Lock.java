package de.mhus.lib.core.concurrent;

import de.mhus.lib.errors.TimeoutRuntimeException;

public class Lock {

	  protected Thread lock = null;
	  protected String name;
	  protected boolean privacy = false;
	
	  public Lock() {}
	  
	  public Lock(String name, boolean privacy) {
		  setName(name);
		  this.privacy = privacy;
	  }
	  
	  public Lock(String name) {
		  setName(name);
	  }
	  
	  public void lock() {
		  synchronized (this) {
			
		    while(isLocked()){
		      try {
				wait();
			} catch (InterruptedException e) {
				
			}
		    }
		    lock = Thread.currentThread();
		  }
	  }

	  public void lockWithException(long timeout) {
		  if (lock(timeout)) return;
		  throw new TimeoutRuntimeException(name);
	  }

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
		    return true;
		  }
	  }
	
	  public boolean unlockOwned(){
		  synchronized (this) {
			if (lock != Thread.currentThread()) return false;
		    lock = null;
		    notify();
		    return true;
		  }
	  }
	  public void unlockHard(){
		  synchronized (this) {
		    lock = null;
		    notify();
		  }
	  }
	  
	  public void waitUntilUnlock() {
		  synchronized (this) {
			    while(isLocked()){
			      try {
					wait();
				} catch (InterruptedException e) {
					
				}
			    }
			  }
	  }
	  
	  public void waitWithException(long timeout) {
		if (waitUntilUnlock(timeout)) return;  
		throw new TimeoutRuntimeException(name);
	  }
	  
	  public boolean waitUntilUnlock(long timeout) {
		  synchronized (this) {
			
		    while(isLocked()){
		    	long start = System.currentTimeMillis();
		      try {
				wait(timeout);
			} catch (InterruptedException e) {
			}
		      if (System.currentTimeMillis() - start >= timeout ) return false;
		    }
		    return true;
		  }
	  }

	public boolean isLocked() {
		return lock != null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Thread getLocker() {
		if (privacy) return null;
		return lock;
	}
	
	public boolean isPrivacy() {
		return privacy;
	}
	
	@Override
	public String toString() {
		return name + (lock != null ? " " + lock.getName() : "");
	}
	  
}
