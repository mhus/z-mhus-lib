package de.mhus.lib.adb.transaction;

import java.util.HashMap;

import de.mhus.lib.adb.Persistable;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.errors.TimeoutRuntimeException;

/**
 * <p>MemoryLockStrategy class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class MemoryLockStrategy extends LockStrategy {

	private long maxLockAge = MTimeInterval.HOUR_IN_MILLISECOUNDS;
	private long sleepTime = 200;

	private HashMap<String, LockObject> locks = new HashMap<>();
	
	/** {@inheritDoc} */
	@Override
	public void lock(Persistable object, String key, Transaction transaction,
			long timeout) {
		
		long start = System.currentTimeMillis();
		while (true) {
			synchronized (this) {
				LockObject current = locks.get(key);
				if (current != null && current.getAge() > maxLockAge) {
					log().i("remove old lock",current.owner,key);
					locks.remove(key);
					continue;
				}
				if (current == null) {
					locks.put(key, new LockObject(transaction));
					return;
				} else {
					log().t("wait for lock",key);
				}
//				if (current != null && current.equals(transaction)) {
//					// already locked by me
//					return;
//				}
			}

			if (System.currentTimeMillis() - start > timeout)
				throw new TimeoutRuntimeException(key);
			MThread.sleep(sleepTime );
			
		}
	}

	/** {@inheritDoc} */
	@Override
	public void releaseLock(Persistable object, String key,
			Transaction transaction) {
		synchronized (this) {
			LockObject obj = locks.get(key);
			if (obj == null) return;
			if (obj.owner.equals(transaction.getName()))
				locks.remove(key);
			else
				log().d("it's not lock owner",key,transaction);
		}
	}

	/**
	 * <p>Getter for the field <code>maxLockAge</code>.</p>
	 *
	 * @return a long.
	 */
	public long getMaxLockAge() {
		return maxLockAge;
	}

	/**
	 * <p>Setter for the field <code>maxLockAge</code>.</p>
	 *
	 * @param maxLockAge a long.
	 */
	public void setMaxLockAge(long maxLockAge) {
		this.maxLockAge = maxLockAge;
	}

	/**
	 * <p>Getter for the field <code>sleepTime</code>.</p>
	 *
	 * @return a long.
	 */
	public long getSleepTime() {
		return sleepTime;
	}

	/**
	 * <p>Setter for the field <code>sleepTime</code>.</p>
	 *
	 * @param sleepTime a long.
	 */
	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}
	
	private class LockObject {
		public LockObject(Transaction transaction) {
			owner = transaction.getName();
		}
		public long getAge() {
			return System.currentTimeMillis() - created;
		}
		private long created = System.currentTimeMillis();
		private String owner;
	}
}
