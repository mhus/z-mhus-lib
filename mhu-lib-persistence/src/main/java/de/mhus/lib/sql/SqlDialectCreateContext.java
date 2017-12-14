package de.mhus.lib.sql;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.query.ACreateContext;

public class SqlDialectCreateContext implements ACreateContext {

	private StringBuffer buffer;
	private DbManager manager;

	public SqlDialectCreateContext(DbManager manager, StringBuffer buffer) {
		this.manager = manager;
		this.buffer = buffer;
	}

	public StringBuffer getBuffer() {
		return buffer;
	}

	public DbManager getManager() {
		return manager;
	}

}
