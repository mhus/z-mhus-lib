package de.mhus.lib.adb.transaction;

import java.util.LinkedList;

import de.mhus.lib.sql.DbConnection;

public class Encapsulation {
	
	private LinkedList<DbConnection> queue = new LinkedList<>();
	
	public void append(DbConnection con) {
		queue.add(con);
	}

	public DbConnection getCurrent() {
		return queue.getLast();
	}

	public void shift() {
		queue.removeLast();
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

}
