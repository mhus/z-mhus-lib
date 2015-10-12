package de.mhus.lib.test.dbconnection;

import java.util.LinkedList;

import de.mhus.lib.core.MThread;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DefaultDbPool;
import de.mhus.lib.sql.MysqlDbProvider;

public class FinalizedMain {

	public static void main(String[] args) throws Exception {

		MysqlDbProvider provider = new MysqlDbProvider("localhost","test","test","test");
		DefaultDbPool pool = new DefaultDbPool(provider);
		DbConnection con = pool.getConnection();
		con = null;
		System.gc();
		
		System.runFinalization();
		
		MThread.sleep(5000);
	}

}
