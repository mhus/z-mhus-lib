package de.mhus.lib.core.base.service;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.concurrent.Lock;

@DefaultImplementation(LockManagerImpl.class)
public interface LockManager {

	Lock getLock(String name);

	/**
	 * Return a list of names with current locks.
	 * @return List
	 */
	String[] currentLocks();
	
}
