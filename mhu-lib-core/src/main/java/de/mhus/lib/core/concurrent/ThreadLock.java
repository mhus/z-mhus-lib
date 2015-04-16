package de.mhus.lib.core.concurrent;

public class ThreadLock extends Lock {

	public ThreadLock() {
		super();
	}

	public ThreadLock(String name) {
		super(name);
	}

	public ThreadLock(String name, boolean privacy) {
		super(name, privacy);
	}

	@Override
	public boolean isLocked() {
		return lock != null && lock != Thread.currentThread();
	}

}
