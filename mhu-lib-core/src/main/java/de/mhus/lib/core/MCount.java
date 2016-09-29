package de.mhus.lib.core;

import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.core.jmx.MJmx;
import de.mhus.lib.core.service.UniqueId;

/**
 * <p>MCount class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@JmxManaged(descrition = "Simple Counter")
public class MCount extends MJmx {

	protected long cnt;
	private String name;
	private long startTime = 0;
	private long lastTime = 0;
	protected boolean isClosed;
	
	/**
	 * <p>Constructor for MCount.</p>
	 */
	public MCount() {
		cnt = 0;
		name = "Counter " + MSingleton.baseLookup(this,UniqueId.class).nextUniqueId();
	}
	
	/**
	 * <p>Constructor for MCount.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public MCount(String name) {
		super(true,name);
		cnt = 0;
		this.name = name;
	}
	
	/**
	 * <p>reset.</p>
	 */
	@JmxManaged(descrition="Reset the counter statistic")
	public void reset() {
		isClosed = false;
		cnt = 0;
		startTime = 0;
		lastTime = 0;
	}
	
	/**
	 * <p>inc.</p>
	 */
	public void inc() {
		if (isClosed) return;
		cnt++;
		lastTime = System.currentTimeMillis();
		if (startTime == 0) startTime = lastTime;
	}

	/**
	 * <p>getValue.</p>
	 *
	 * @return a long.
	 */
	@JmxManaged(descrition = "Amount of counts")
	public long getValue() {
		return cnt;
	}
	
	/**
	 * <p>getHitsPerSecond.</p>
	 *
	 * @return a double.
	 */
	public double getHitsPerSecond() {
		if (startTime == 0 || lastTime == 0 || cnt == 0) return 0;
		return (double)cnt / (double)((lastTime - startTime)/1000);
	}
	
	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@JmxManaged(descrition = "Name of this value")
	public String getName() {
		return name;
	}
	
	/**
	 * <p>getFirstHitTime.</p>
	 *
	 * @return a long.
	 */
	public long getFirstHitTime() {
		return startTime;
	}
	
	/**
	 * <p>getLastHitTime.</p>
	 *
	 * @return a long.
	 */
	public long getLastHitTime() {
		return lastTime;
	}
	
	/**
	 * <p>getStatusAsString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@JmxManaged(descrition = "Readable status of the counter")
	public String getStatusAsString() {
		if (startTime == 0 || lastTime == 0 || cnt == 0) return "unused";
		return MDate.toIsoDateTime(getFirstHitTime()) + " - " + MDate.toIsoDateTime(getLastHitTime()) + "," + getHitsPerSecond() + " hits/sec," + cnt;
	}
	
	/**
	 * <p>close.</p>
	 */
	public void close() {
		if (isClosed) return;
		isClosed = true;
		log().i("close",name,cnt,getHitsPerSecond());
	}
	
	/** {@inheritDoc} */
	@Override
	protected void finalize() {
		close();
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return MSystem.toString(this, getStatusAsString());
	}
}
