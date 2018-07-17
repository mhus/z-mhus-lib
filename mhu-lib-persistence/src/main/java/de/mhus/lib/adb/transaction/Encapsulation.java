package de.mhus.lib.adb.transaction;

import java.util.LinkedList;

import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.sql.DbConnection;

public class Encapsulation {
	
	private LinkedList<DbConnection> queue = new LinkedList<>();
	
	public void append(DbConnection con) {
		queue.add(con);
	}

	public DbConnection getCurrent() {
		if (queue.isEmpty()) return null;
		return queue.getLast();
	}

	public void shift() {
		if (queue.isEmpty()) return;
		DbConnection last = queue.removeLast();
		// for secure the default behavior
		try {
			if (last != null)
				last.commit();
		} catch (Throwable e) {
			MLogUtil.log().w(e);
		}
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

}
