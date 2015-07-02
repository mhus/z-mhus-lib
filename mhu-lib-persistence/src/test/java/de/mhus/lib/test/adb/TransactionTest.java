package de.mhus.lib.test.adb;

import junit.framework.TestCase;
import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.DbSchema;
import de.mhus.lib.adb.DbTransaction;
import de.mhus.lib.adb.transaction.TransactionNestedException;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.config.NodeConfig;
import de.mhus.lib.core.util.ObjectContainer;
import de.mhus.lib.sql.DbPool;
import de.mhus.lib.sql.DbPoolBundle;
import de.mhus.lib.test.adb.model.TransactionDummy;
import de.mhus.lib.test.adb.model.TransactionSchema;

public class TransactionTest extends TestCase {

	public DbPoolBundle createPool(String name) {
		NodeConfig cdb = new NodeConfig();
		NodeConfig cconfig = new NodeConfig();
		NodeConfig ccon = new NodeConfig();
		ccon.setProperty("driver", "org.hsqldb.jdbcDriver");
		ccon.setProperty("url", "jdbc:hsqldb:mem:" + name);
		ccon.setProperty("user", "sa");
		ccon.setProperty("pass", "");
		cdb.setConfig("connection", ccon);
		cconfig.setConfig("test", cdb);
		DbPoolBundle pool = new DbPoolBundle(cconfig,null);
		return pool;
	}
	
	public DbManager createManager() throws Exception {
		DbPool pool = createPool("transactionModel").getPool("test");
		DbSchema schema = new TransactionSchema();
		DbManager manager = new DbManager(pool, schema);
		return manager;
	}

	public void testLock() throws Exception {
		
		DbManager manager = createManager();
		final TransactionDummy obj1 = manager.inject(new TransactionDummy());
		final TransactionDummy obj2 = manager.inject(new TransactionDummy());
		final TransactionDummy obj3 = manager.inject(new TransactionDummy());
		
		obj1.save();
		obj2.save();
		obj3.save();
		
		DbTransaction.lock(obj1,obj2); // simple lock
		DbTransaction.release();
		
		DbTransaction.release(); // one more should be ok - robust code
		
		DbTransaction.lock(obj1,obj2);
		try {
			DbTransaction.lock(obj3); // nested not locked should fail, can't lock two times - philosophers deadlock
			DbTransaction.release();
			fail("Nested Transaction Not Allowed");
		} catch (TransactionNestedException e) {
			System.out.println(e);
		}
		DbTransaction.release();

		DbTransaction.lock(obj1,obj2);
		DbTransaction.lock(obj1); // nested is ok as long as it is already locked - no philosophers problem
		DbTransaction.release();
		DbTransaction.release();

		// concurrent locking ...
		DbTransaction.lock(obj1,obj2);

		final ObjectContainer<Boolean> done = new ObjectContainer<>(false);
		final ObjectContainer<String> fail = new ObjectContainer<>();
		
		new MThread(new Runnable() {
			
			@Override
			public void run() {
				// concurrent
				try {
					DbTransaction.lock(2000, obj1,obj2);
					fail.setObject("Concurrent Lock Possible");
					return;
				} catch (Throwable t) {
					System.out.println(t);
				} finally {
					DbTransaction.release();
				}
				
				// not concurrent
				DbTransaction.lock(2000, obj3);
				DbTransaction.release();
				
				done.setObject(true);
			}
		}).start();
		
		while (done.getObject() == false && fail.getObject() == null)
			MThread.sleep(200);

		if (fail.getObject() != null)
			fail(fail.getObject());
		
		DbTransaction.release();
		
	}
	
}
