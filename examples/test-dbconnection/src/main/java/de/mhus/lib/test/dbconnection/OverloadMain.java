package de.mhus.lib.test.dbconnection;

import java.util.LinkedList;

import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DefaultDbPool;
import de.mhus.lib.sql.MysqlDbProvider;

public class OverloadMain {

	public static void main(String[] args) throws Exception {

		LinkedList<DbConnection> connections = new LinkedList<DbConnection>();
		
		MysqlDbProvider provider = new MysqlDbProvider("localhost","test","test","test");
		DefaultDbPool pool = new DefaultDbPool(provider);
		while (true) {
			DbConnection con = pool.getConnection();
			connections.add(con);
		}
		
	}

}
