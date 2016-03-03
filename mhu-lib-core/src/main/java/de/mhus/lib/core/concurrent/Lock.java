package de.mhus.lib.core.concurrent;

import de.mhus.lib.errors.TimeoutRuntimeException;

/**
 * <p>Lock class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class Lock {

	  protected Thread lock = null;
	  protected String name;
	  protected boolean privacy = false;
	
	  /**
	   * <p>Constructor for Lock.</p>
	   */
	  public Lock() {}
	  
	  /**
	   * <p>Constructor for Lock.</p>
	   *
	   * @param name a {@link java.lang.String} object.
	   * @param privacy a boolean.
	   */
	  public Lock(String name, boolean privacy) {
		  setName(name);
		  this.privacy = privacy;
	  }
	  
	  /**
	   * <p>Constructor for Lock.</p>
	   *
	   * @param name a {@link java.lang.String} object.
	   */
	  public Lock(String name) {
		  setName(name);
	  }
	  
	  /**
	   * <p>lock.</p>
	   */
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

	  /**
	   * <p>lockWithException.</p>
	   *
	   * @param timeout a long.
	   */
	  public void lockWithException(long timeout) {
		  if (lock(timeout)) return;
		  throw new TimeoutRuntimeException(name);
	  }

	  /**
	   * <p>lock.</p>
	   *
	   * @param timeout a long.
	   * @return a boolean.
	   */
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
	
	  /**
	   * <p>unlockOwned.</p>
	   *
	   * @return a boolean.
	   */
	  public boolean unlockOwned(){
		  synchronized (this) {
			if (lock != Thread.currentThread()) return false;
		    lock = null;
		    notify();
		    return true;
		  }
	  }
	  /**
	   * <p>unlockHard.</p>
	   */
	  public void unlockHard(){
		  synchronized (this) {
		    lock = null;
		    notify();
		  }
	  }
	  
	  /**
	   * <p>waitUntilUnlock.</p>
	   */
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
	  
	  /**
	   * <p>waitWithException.</p>
	   *
	   * @param timeout a long.
	   */
	  public void waitWithException(long timeout) {
		if (waitUntilUnlock(timeout)) return;  
		throw new TimeoutRuntimeException(name);
	  }
	  
	  /**
	   * <p>waitUntilUnlock.</p>
	   *
	   * @param timeout a long.
	   * @return a boolean.
	   */
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

	/**
	 * <p>isLocked.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isLocked() {
		return lock != null;
	}

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * <p>getLocker.</p>
	 *
	 * @return a {@link java.lang.Thread} object.
	 */
	public Thread getLocker() {
		if (privacy) return null;
		return lock;
	}
	
	/**
	 * <p>isPrivacy.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isPrivacy() {
		return privacy;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return name + (lock != null ? " " + lock.getName() : "");
	}
	  
}
