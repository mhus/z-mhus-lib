package de.mhus.lib.core.concurrent;

/**
 * <p>ThreadLock class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ThreadLock extends Lock {

	/**
	 * <p>Constructor for ThreadLock.</p>
	 */
	public ThreadLock() {
		super();
	}

	/**
	 * <p>Constructor for ThreadLock.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public ThreadLock(String name) {
		super(name);
	}

	/**
	 * <p>Constructor for ThreadLock.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param privacy a boolean.
	 */
	public ThreadLock(String name, boolean privacy) {
		super(name, privacy);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isLocked() {
		return lock != null && lock != Thread.currentThread();
	}

}
