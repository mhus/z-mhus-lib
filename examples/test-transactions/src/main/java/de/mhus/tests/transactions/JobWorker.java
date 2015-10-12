package de.mhus.tests.transactions;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.DbTransaction;
import de.mhus.lib.core.MThread;

public class JobWorker implements Runnable {

	private DbManager manager;
	private int nr;
	private DbObj o1;

	public JobWorker(DbManager manager, int nr, DbObj o) {
		this.manager = manager;
		this.nr = nr;
		this.o1 = o;
	}
	
	public void run() {
		
		try {
			
			while (true) {
				DbTransaction.lock(o1);
				System.out.println(">>> " + nr);

				// MThread.sleep( (int)(Math.random() * 1000 ) );
				
				for (int i = 0; i < 10; i++)
					doSomethingElse(o1);
				
				System.out.println("<<< " + nr);
				DbTransaction.release();
				
				MThread.sleep(100);
			}
			
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		
	}

	private void doSomethingElse(DbObj o1) {

		DbTransaction.lock(o1);
		MThread.sleep( (int)(Math.random() * 1000 ) );
		DbTransaction.release();

	}

}
