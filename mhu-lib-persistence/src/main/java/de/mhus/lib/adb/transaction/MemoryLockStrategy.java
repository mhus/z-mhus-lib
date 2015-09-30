package de.mhus.lib.adb.transaction;

import java.util.HashMap;

import de.mhus.lib.adb.Persistable;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.errors.TimeoutRuntimeException;

public class MemoryLockStrategy extends LockStrategy {

	private long maxLockAge = MTimeInterval.HOUR_IN_MILLISECOUNDS;
	private long sleepTime = 200;

	private HashMap<String, LockObject> locks = new HashMap<>();
	
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

	public long getMaxLockAge() {
		return maxLockAge;
	}

	public void setMaxLockAge(long maxLockAge) {
		this.maxLockAge = maxLockAge;
	}

	public long getSleepTime() {
		return sleepTime;
	}

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
