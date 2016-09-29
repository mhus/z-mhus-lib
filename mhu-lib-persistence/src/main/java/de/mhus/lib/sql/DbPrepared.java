package de.mhus.lib.sql;

import de.mhus.lib.core.parser.CompiledString;
import de.mhus.lib.errors.MException;

/**
 * This define a prepared statement connected to a pool. The main differentz to
 * DbStatement, it's not connected to a DbConnection by creation time. But it will
 * precompile the query string.
 * 
 * @author mikehummel
 *
 */
public class DbPrepared {

	private DbPool pool;
	private CompiledString query;

	DbPrepared(DbPool pool, String queryString, String language) throws MException {
		this.pool = pool;
		query = pool.getDialect().getQueryParser(language).compileString(queryString);
		//		query = new SimpleQueryParser().compileString(queryString);
		//		query = new SqlCompiler().compileString(queryString);
	}

	/**
	 * Return a statement with a new connection from the pool.
	 * 
	 * @return x
	 * @throws Exception
	 */
	public DbStatement getStatement() throws Exception {
		DbConnection con = pool.getConnection();
		return con.createStatement(this);
	}

	/**
	 * Return a statement for the given connection - hopefully from the same pool.
	 * 
	 * @param con
	 * @return x
	 * @throws Exception
	 */
	public DbStatement getStatement(DbConnection con) throws Exception {
		return con.createStatement(this);
	}

	CompiledString getQuery() {
		return query;
	}

}
