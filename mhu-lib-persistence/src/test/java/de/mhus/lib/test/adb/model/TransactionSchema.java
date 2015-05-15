package de.mhus.lib.test.adb.model;

import java.util.List;

import de.mhus.lib.adb.DbSchema;
import de.mhus.lib.adb.Persistable;
import de.mhus.lib.adb.transaction.MemoryLockStrategy;

public class TransactionSchema extends DbSchema {

	public TransactionSchema() {
		lockStrategy = new MemoryLockStrategy();
	}
	
	@Override
	public void findObjectTypes(List<Class<? extends Persistable>> list) {
		list.add(TransactionDummy.class);
	}

}
