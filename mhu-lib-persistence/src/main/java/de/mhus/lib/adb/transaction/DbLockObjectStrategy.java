package de.mhus.lib.adb.transaction;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.Persistable;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.TimeoutRuntimeException;

public class DbLockObjectStrategy extends LockStrategy {

	private long maxLockAge = MTimeInterval.HOUR_IN_MILLISECOUNDS;
	private long sleepTime = 200;

	@Override
	public void lock(Persistable object, String key,
			Transaction transaction, long timeout) {
		DbLockObject lock = transaction.getDbManager().inject(new DbLockObject());
//		if (key.length() > 760) key = key.substring(0, 760); // not really a good solution ...!
		lock.setKey(key);
		lock.setOwner(transaction.getName());
		long start = System.currentTimeMillis();
		while (true) {
			try {
				lock.save();
				return;
			} catch (MException e) {
				log().d(e);
			}
			// check age of lock entry
			try {
				DbLockObject obj = transaction.getDbManager().getObject(DbLockObject.class, key);
				if (obj != null && obj.getAge() > maxLockAge) {
					log().i("remove old lock",obj.getOwner(),key);
					obj.delete();
					continue;
				}
			} catch (MException e) {
				log().d(e);
			}
			
			if (System.currentTimeMillis() - start > timeout)
				throw new TimeoutRuntimeException(key);
			MThread.sleep(sleepTime );
		}
	}

	@Override
	public void releaseLock(Persistable object, String key,
			Transaction transaction) {
		try {
			DbLockObject obj = transaction.getDbManager().getObject(DbLockObject.class, key);
			if (obj != null) {
				if (obj.getOwner().equals(transaction.getName()))
					obj.delete();
				else
					log().d("it's not lock owner",key,transaction);
			}
		} catch (MException e) {
			log().d(e);
		}
	}

	public void cleanup(DbManager manager) throws MException {
		for (DbLockObject o : manager.getAll(DbLockObject.class))
				o.delete();
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

}
