package de.mhus.lib.core;

import de.mhus.lib.annotations.jmx.JmxManaged;

@JmxManaged(descrition = "Counter with delay")
public class MCountWithDelay extends MCount {

	private long sleepInterval = 0;
	private int  sleepSeconds  = 0;
	private boolean throwExceptionOnNextCount = false;
	
	public MCountWithDelay() {
		super();
	}
	public MCountWithDelay(String name) {
		super(name);
	}
	
	@JmxManaged(descrition="Get the interval")
	public long getSleepInterval() {
		return sleepInterval;
	}
	@JmxManaged(descrition="Set the interval after it will sleeping")
	public void setSleepInterval(long sleepInterval) {
		this.sleepInterval = sleepInterval;
	}
	@JmxManaged(descrition="Get the seconds to sleep each interval")
	public int getSleepSeconds() {
		return sleepSeconds;
	}
	@JmxManaged(descrition="Set the seconds to sleep each interval")
	public void setSleepSeconds(int sleepSeconds) {
		this.sleepSeconds = sleepSeconds;
	}
	
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

	@JmxManaged(descrition="Should the counter create and throw a RunntimeException() on next count")
	public boolean isThrowExceptionOnNextCount() {
		return throwExceptionOnNextCount;
	}
	@JmxManaged(descrition="Should the counter create and throw a RunntimeException() on next count")
	public void setThrowExceptionOnNextCount(boolean throwExceptionOnNextCount) {
		this.throwExceptionOnNextCount = throwExceptionOnNextCount;
	}
	
}
