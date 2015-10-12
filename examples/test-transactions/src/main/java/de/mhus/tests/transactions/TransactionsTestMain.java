package de.mhus.tests.transactions;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.DbSchema;
import de.mhus.lib.adb.DbTransaction;
import de.mhus.lib.adb.transaction.Transaction;
import de.mhus.lib.core.MThread;
import de.mhus.lib.sql.DefaultDbPool;
import de.mhus.lib.sql.HsqlDbProvider;
import de.mhus.lib.sql.JdbcProvider;

public class TransactionsTestMain {

	public static void main(String[] args) throws Exception {
		
		DbSchema schema = new MySchema();
		DefaultDbPool pool = new DefaultDbPool(new HsqlDbProvider());
		DbManager manager = new DbManager(pool, schema);

		DbObj o1 = manager.inject(new DbObj());
		o1.save();

		for (int i = 0; i < 100; i++) {
			JobWorker job = new JobWorker(manager, i, o1);
			new MThread(job).start();
		}
		
		
		while(true) {
			MThread.sleep(1000);
		}
		
	}

}
