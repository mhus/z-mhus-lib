package de.mhus.lib.core.base.service;

import java.util.HashMap;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.concurrent.Lock;
import de.mhus.lib.core.util.SoftHashMap;

public class LockManagerImpl extends MLog implements LockManager {
	
	private HashMap<String, Lock> locks = new HashMap<>();
	private SoftHashMap<String, Lock> cache = new SoftHashMap<>();
	
	public LockManagerImpl() {
		log().i("Start DefaultLockManager");
	}
	
	@Override
	public Lock getLock(String name) {
		Lock lock = null;
		synchronized (cache) {
			lock = cache.get(name);
			if (lock == null) {
				lock = new Lock(name, false);
				cache.put(name, lock);
			}
		}
		return lock;
	}

	class ManagerLock extends Lock {
	    @Override
		protected void lockEvent(boolean locked) {
	    		synchronized (cache) {
	    			if (locked) {
	    				locks.put(name, this);
	    				if (!cache.containsKey(name)) // could be removed in the meantime ?
	    					cache.put(name, this);
	    			} else {
	    				locks.remove(name);
	    			}
			}
		}
	}

	@Override
	public String[] currentLocks() {
		synchronized (cache) {
			return locks.keySet().toArray(new String[locks.size()]);
		}
	}
	
}
