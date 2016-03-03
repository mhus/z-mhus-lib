package de.mhus.lib.adb.transaction;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.Persistable;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.TimeoutRuntimeException;

/**
 * <p>DbLockObjectStrategy class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class DbLockObjectStrategy extends LockStrategy {

	private long maxLockAge = MTimeInterval.HOUR_IN_MILLISECOUNDS;
	private long sleepTime = 200;

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
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

	/**
	 * <p>cleanup.</p>
	 *
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void cleanup(DbManager manager) throws MException {
		for (DbLockObject o : manager.getAll(DbLockObject.class))
				o.delete();
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

}
