package de.mhus.lib.adb.transaction;

import java.util.Map;
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

	public TransactionLock(DbManager manager, Persistable ... objects) {
		this.manager = manager;
		this.objects = objects;
	}
	
	public TransactionLock(Persistable ... objects) {
		this.objects = objects;
		manager = null;
		for (Persistable o : objects)
			if (o instanceof DbObject) {
				manager = ((DbObject)o).getDbManager();
				break;
			}
	}

	@Override
	public void lock(long timeout)  throws TimeoutRuntimeException {
		if (objects == null) throw new NotSupportedException("Transaction already gone");
		if (manager == null) throw new NotSupportedException("DbManager not found, need direct manager or DbObject implementation to grep the manager");
		LockStrategy strategy = manager.getSchema().getLockStrategy();
		if (strategy == null) return;

		TreeMap<String, Persistable> ordered = new TreeMap<>();
		for (Persistable o : objects) {
			String key = createKey(o);
			ordered.put(key, o);
		}
		
		long start = System.currentTimeMillis();
		for (Map.Entry<String, Persistable> entry : ordered.entrySet()) {
			try {
				strategy.lock(entry.getValue(), entry.getKey(), this, timeout);
			} catch (Throwable t) {log().d(t);}
			if (System.currentTimeMillis() - start > timeout) {
				for (Map.Entry<String, Persistable> entry2 : ordered.entrySet()) {
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
		if (o instanceof DbObject) m = ((DbObject)o).getDbManager();
		
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
	protected void finalize() {
		//TODO error message !
		release();
	}

	@Override
	public DbManager getDbManager() {
		return manager;
	}

}
