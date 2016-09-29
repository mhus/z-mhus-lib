package de.mhus.lib.adb.transaction;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.DbObject;
import de.mhus.lib.adb.Persistable;
import de.mhus.lib.adb.model.Field;
import de.mhus.lib.adb.model.Table;
import de.mhus.lib.errors.NotSupportedException;
import de.mhus.lib.errors.TimeoutRuntimeException;

public class TransactionLock extends Transaction {

	private Persistable[] objects;
	private DbManager manager;
	private boolean locked;
	private TreeMap<String, Persistable> orderedKeys;

	public TransactionLock(DbManager manager, Persistable ... objects) {
		this.manager = manager;
		this.objects = objects;
	}
	
	public TransactionLock(Persistable ... objects) {
		this.objects = objects;
		manager = null;
		for (Persistable o : objects)
			if (o instanceof DbObject) {
				manager = (DbManager) ((DbObject)o).getDbHandler();
				break;
			}
	}

	@Override
	public void lock(long timeout)  throws TimeoutRuntimeException {
		if (objects == null) throw new NotSupportedException("Transaction already gone");
		if (manager == null) throw new NotSupportedException("DbManager not found, need direct manager or DbObject implementation to grep the manager");
		LockStrategy strategy = manager.getSchema().getLockStrategy();
		if (strategy == null) return;

		getLockKeys();
		
		long start = System.currentTimeMillis();
		for (Map.Entry<String, Persistable> entry : orderedKeys.entrySet()) {
			try {
				strategy.lock(entry.getValue(), entry.getKey(), this, timeout);
			} catch (Throwable t) {log().d(t);}
			if (System.currentTimeMillis() - start > timeout) {
				for (Map.Entry<String, Persistable> entry2 : orderedKeys.entrySet()) {
					try {
						strategy.releaseLock(entry2.getValue(), entry2.getKey(), this);
					} catch (Throwable t) {log().d(t);}
				}
				throw new TimeoutRuntimeException();
			}
				
		}
		
		locked = true;
	}

	protected String createKey(Persistable o) {
		// find db manager of the object, fallback is my manager
		DbManager m = manager;
		if (o instanceof DbObject) m = (DbManager) ((DbObject)o).getDbHandler();
		
		String regName = m.getRegistryName(o);
		Table table = m.getTable(regName);
		StringBuffer key = new StringBuffer().append(regName);
		for (Field pKey : table.getPrimaryKeys()) {
			String value = "";
			try {
				value = String.valueOf(pKey.get(o));
			} catch (Exception e) {log().d(e);}
			key.append(",").append(value);
		}
		return key.toString();
	}

	@Override
	public void release() {
		if (!locked || objects == null || manager == null) return;
		
		LockStrategy strategy = manager.getSchema().getLockStrategy();
		if (strategy == null) return;
		
		for (Persistable o : objects) {
			String key = createKey(o);
			try {
				strategy.releaseLock(o, key, this);
			} catch (Throwable t) {log().d(t);}
		}
		
		manager = null;
		objects = null;
		locked = false;
		
	}
	
	@Override
	public synchronized void pushNestedLock(Transaction transaction) {
		// validate lock objects
		Set<String> keys = transaction.getLockKeys();
		getLockKeys();
		if (keys != null) {
			for (String key : keys) {
				if (!orderedKeys.containsKey(key)) {
					throw new TransactionNestedException("Nested Key Not Locked in MainLock: " + key);
				}
			}
		}
		super.pushNestedLock(transaction);
	}
	
	@Override
	protected void finalize() {
		//TODO error message !
		release();
	}

	@Override
	public DbManager getDbManager() {
		return manager;
	}

	@Override
	public synchronized Set<String> getLockKeys() {
		if (orderedKeys == null) {
			orderedKeys = new TreeMap<>();
			if (objects != null) {
				for (Persistable o : objects) {
					String key = createKey(o);
					orderedKeys.put(key, o);
				}
			}
		}
		return orderedKeys.keySet();
	}

}
