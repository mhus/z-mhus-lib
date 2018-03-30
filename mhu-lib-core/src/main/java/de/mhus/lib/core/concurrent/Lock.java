/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	
	  /**
	   * Run the given task in a locked environment.
	   * @param task
	   */
	  public void lock(Runnable task) {
		  try {
			  lock();
			  task.run();
		  } finally {
			  unlock();
		  }
	  }
	  
	  /**
	   * Run the given task in a locked environment.
	   * @param task
	   * @param timeout 
	   */
	  public void lock(Runnable task, long timeout) {
		  try {
			  lockWithException(timeout);
			  task.run();
		  } finally {
			  unlock();
		  }
	  }
	  
	  /**
	   * Unlock if the current thread is also the owner.
	   * 
	   * @return true if lock wass successful
	   */
	  public boolean unlock(){
		  synchronized (this) {
			if (lock != Thread.currentThread()) return false;
		    lock = null;
		    notify();
		    return true;
		  }
	  }
	  
	  /**
	   * Unlock in every case !!! This can break a locked area.
	   * 
	   */
	  public void unlockHard(){
		  synchronized (this) {
		    lock = null;
		    notify();
		  }
	  }
	  
	  /**
	   * Sleeps until the lock is released.
	   * 
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
