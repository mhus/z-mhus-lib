package de.mhus.tests.transactions;

import java.util.List;

import de.mhus.lib.adb.DbSchema;
import de.mhus.lib.adb.Persistable;
import de.mhus.lib.adb.transaction.MemoryLockStrategy;

public class MySchema extends DbSchema {

	public MySchema() {
		lockStrategy = new MemoryLockStrategy();
	}
	
	@Override
	public void findObjectTypes(List<Class<? extends Persistable>> list) {
		list.add(DbObj.class);
	}

}
