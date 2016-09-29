package de.mhus.lib.sql;

import de.mhus.lib.core.parser.Parser;
import de.mhus.lib.errors.MException;

/**
 * This class is a proxy for DbProvider and it is the public interface for it. It will automatic free
 * the provider in the pool if the connection is no more needed.
 * 
 * @author mikehummel
 *
 */
public interface DbConnection {

	public static final String LANGUAGE_COMMON = "common";

	/**
	 * Commit this connection changes. The system should not use the auto commit from jdbc.
	 * 
	 * @throws Exception
	 */
	public void commit() throws Exception;

	/**
	 * Returns if the user can change the database.
	 * 
	 * @return x
	 * @throws Exception
	 */
	public boolean isReadOnly() throws Exception;

	/**
	 * Rollback the changes in the connection.
	 * 
	 * @throws Exception
	 */
	public void rollback() throws Exception;

	/**
	 * Returns a predefined statement.
	 * 
	 * @param name Name of the predefined statement.
	 * @return x The statement or null if the statement is not found.
	 * @throws MException
	 */
	public DbStatement getStatement(String name) throws MException;

	/**
	 * Create the default language using the detected language. Default is
	 * the default language.
	 * 
	 * @param sql
	 * @return x
	 * @throws MException
	 */
	public DbStatement createStatement(String sql) throws MException;

	/**
	 * Create and returns a new statement. Throws an exception if the language is unknown.
	 * 
	 * @param sql sql query
	 * @param language the specific language or null if the default language should be used.
	 * @return x
	 * @throws MException
	 */
	public DbStatement createStatement(String sql, String language) throws MException;

	/**
	 * Create a statement from a prepared one.
	 * 
	 * @param dbPrepared
	 * @return x
	 */
	DbStatement createStatement(DbPrepared dbPrepared);

	/**
	 * Returns true of the connection is no more valid.
	 * 
	 * @return x
	 */
	public boolean isClosed();

	/**
	 * Returns true if the connection is in use. This is needed for the db connection pool.
	 * 
	 * @return x
	 */
	public boolean isUsed();

	/**
	 * Do not use this function. It will be used by the connection pool.
	 * 
	 * @param used
	 */
	public void setUsed(boolean used);

	/**
	 * Close the connection.
	 */
	public void close();

	/**
	 * Return the unique id of this connection. Need to log information.
	 * 
	 * @return x
	 */
	public long getInstanceId();

	/**
	 * Create a query compiler for this connection. Used by the DbStatement class.
	 * @param language 
	 * @return x
	 * @throws MException
	 */
	public Parser createQueryCompiler(String language) throws MException;

	/**
	 * Return the original instance thrue proxies - if possible.
	 * 
	 * @return x
	 */
	public DbConnection instance();

//	public void setUsedTrace(StackTraceElement[] createStackTrace);

//	public StackTraceElement[] getUsedTrace();

	/**
	 * Return the default used language for this kind of connection.
	 * 
	 * @return x The default supported language, could also be null as default value.
	 */
	public abstract String getDefaultLanguage();

	/**
	 * Return a list of supported languages. Every connection should at least
	 * support the LANGUAGE_COMMON.
	 * 
	 * @return x The list of languages or an empty array but never null.
	 */
	public abstract String[] getLanguages();

}
