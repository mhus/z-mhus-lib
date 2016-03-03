package de.mhus.lib.core;

import de.mhus.lib.annotations.jmx.JmxManaged;

/**
 * <p>MCountWithDelay class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@JmxManaged(descrition = "Counter with delay")
public class MCountWithDelay extends MCount {

	private long sleepInterval = 0;
	private int  sleepSeconds  = 0;
	private boolean throwExceptionOnNextCount = false;
	
	/**
	 * <p>Constructor for MCountWithDelay.</p>
	 */
	public MCountWithDelay() {
		super();
	}
	/**
	 * <p>Constructor for MCountWithDelay.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public MCountWithDelay(String name) {
		super(name);
	}
	
	/**
	 * <p>Getter for the field <code>sleepInterval</code>.</p>
	 *
	 * @return a long.
	 */
	@JmxManaged(descrition="Get the interval")
	public long getSleepInterval() {
		return sleepInterval;
	}
	/**
	 * <p>Setter for the field <code>sleepInterval</code>.</p>
	 *
	 * @param sleepInterval a long.
	 */
	@JmxManaged(descrition="Set the interval after it will sleeping")
	public void setSleepInterval(long sleepInterval) {
		this.sleepInterval = sleepInterval;
	}
	/**
	 * <p>Getter for the field <code>sleepSeconds</code>.</p>
	 *
	 * @return a int.
	 */
	@JmxManaged(descrition="Get the seconds to sleep each interval")
	public int getSleepSeconds() {
		return sleepSeconds;
	}
	/**
	 * <p>Setter for the field <code>sleepSeconds</code>.</p>
	 *
	 * @param sleepSeconds a int.
	 */
	@JmxManaged(descrition="Set the seconds to sleep each interval")
	public void setSleepSeconds(int sleepSeconds) {
		this.sleepSeconds = sleepSeconds;
	}
	
	/** {@inheritDoc} */
	@Override
	public void inc() {
		super.inc();
		if (throwExceptionOnNextCount) {
			throwExceptionOnNextCount = false;
			throw new RuntimeException("Counter " + getName() + " is thrown by request at " + getValue());
		}
		if (isClosed) return;
		if (sleepInterval > 0 && sleepSeconds > 0 && cnt % sleepInterval == 0) {
			log().i(getName(),"Sleep", sleepSeconds);
			MThread.sleep(sleepSeconds * 1000);
		}
	}

	/**
	 * <p>isThrowExceptionOnNextCount.</p>
	 *
	 * @return a boolean.
	 */
	@JmxManaged(descrition="Should the counter create and throw a RunntimeException() on next count")
	public boolean isThrowExceptionOnNextCount() {
		return throwExceptionOnNextCount;
	}
	/**
	 * <p>Setter for the field <code>throwExceptionOnNextCount</code>.</p>
	 *
	 * @param throwExceptionOnNextCount a boolean.
	 */
	@JmxManaged(descrition="Should the counter create and throw a RunntimeException() on next count")
	public void setThrowExceptionOnNextCount(boolean throwExceptionOnNextCount) {
		this.throwExceptionOnNextCount = throwExceptionOnNextCount;
	}
	
}
