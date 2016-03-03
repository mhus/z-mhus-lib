package de.mhus.lib.adb.transaction;

import java.util.Set;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.errors.NotSupportedException;
import de.mhus.lib.errors.TimeoutRuntimeException;

/**
 * <p>TransactionEncapsulation class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class TransactionEncapsulation extends Transaction {

	/** {@inheritDoc} */
	@Override
	public void release() {
		throw new NotSupportedException();
	}

	/** {@inheritDoc} */
	@Override
	public void lock(long timeout) throws TimeoutRuntimeException {
		throw new NotSupportedException();
	}

	/** {@inheritDoc} */
	@Override
	public DbManager getDbManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public Set<String> getLockKeys() {
		// TODO Auto-generated method stub
		return null;
	}

}
