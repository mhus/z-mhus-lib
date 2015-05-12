package de.mhus.lib.adb.transaction;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.errors.NotSupportedException;
import de.mhus.lib.errors.TimeoutRuntimeException;

public class TransactionEncapsulation extends Transaction {

	@Override
	public void release() {
		throw new NotSupportedException();
	}

	@Override
	public void lock(long timeout) throws TimeoutRuntimeException {
		throw new NotSupportedException();
	}

	@Override
	public DbManager getDbManager() {
		// TODO Auto-generated method stub
		return null;
	}

}
