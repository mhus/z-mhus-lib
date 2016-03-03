package de.mhus.lib.sql;

import de.mhus.lib.core.parser.Parser;
import de.mhus.lib.errors.MException;

/**
 * This class is a proxy for DbProvider and it is the public interface for it. It will automatic free
 * the provider in the pool if the connection is no more needed.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface DbConnection {

	/** Constant <code>LANGUAGE_COMMON="common"</code> */
	public static final String LANGUAGE_COMMON = "common";

	/**
	 * Commit this connection changes. The system should not use the auto commit from jdbc.
	 *
	 * @throws java.lang.Exception if any.
	 */
	public void commit() throws Exception;

	/**
	 * Returns if the user can change the database.
	 *
	 * @throws java.lang.Exception if any.
	 * @return a boolean.
	 */
	public boolean isReadOnly() throws Exception;

	/**
	 * Rollback the changes in the connection.
	 *
	 * @throws java.lang.Exception if any.
	 */
	public void rollback() throws Exception;

	/**
	 * Returns a predefined statement.
	 *
	 * @param name Name of the predefined statement.
	 * @return The statement or null if the statement is not found.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public DbStatement getStatement(String name) throws MException;

	/**
	 * Create the default language using the detected language. Default is
	 * the default language.
	 *
	 * @param sql a {@link java.lang.String} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 * @return a {@link de.mhus.lib.sql.DbStatement} object.
	 */
	public DbStatement createStatement(String sql) throws MException;

	/**
	 * Create and returns a new statement. Throws an exception if the language is unknown.
	 *
	 * @param sql sql query
	 * @param language the specific language or null if the default language should be used.
	 * @throws de.mhus.lib.errors.MException if any.
	 * @return a {@link de.mhus.lib.sql.DbStatement} object.
	 */
	public DbStatement createStatement(String sql, String language) throws MException;

	/**
	 * Create a statement from a prepared one.
	 *
	 * @param dbPrepared a {@link de.mhus.lib.sql.DbPrepared} object.
	 * @return a {@link de.mhus.lib.sql.DbStatement} object.
	 */
	DbStatement createStatement(DbPrepared dbPrepared);

	/**
	 * Returns true of the connection is no more valid.
	 *
	 * @return a boolean.
	 */
	public boolean isClosed();

	/**
	 * Returns true if the connection is in use. This is needed for the db connection pool.
	 *
	 * @return a boolean.
	 */
	public boolean isUsed();

	/**
	 * Do not use this function. It will be used by the connection pool.
	 *
	 * @param used a boolean.
	 */
	public void setUsed(boolean used);

	/**
	 * Close the connection.
	 */
	public void close();

	/**
	 * Return the unique id of this connection. Need to log information.
	 *
	 * @return a long.
	 */
	public long getInstanceId();

	/**
	 * Create a query compiler for this connection. Used by the DbStatement class.
	 *
	 * @param language a {@link java.lang.String} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 * @return a {@link de.mhus.lib.core.parser.Parser} object.
	 */
	public Parser createQueryCompiler(String language) throws MException;

	/**
	 * Return the original instance thrue proxies - if possible.
	 *
	 * @return a {@link de.mhus.lib.sql.DbConnection} object.
	 */
	public DbConnection instance();

//	public void setUsedTrace(StackTraceElement[] createStackTrace);

//	public StackTraceElement[] getUsedTrace();

	/**
	 * Return the default used language for this kind of connection.
	 *
	 * @return The default supported language, could also be null as default value.
	 */
	public abstract String getDefaultLanguage();

	/**
	 * Return a list of supported languages. Every connection should at least
	 * support the LANGUAGE_COMMON.
	 *
	 * @return The list of languages or an empty array but never null.
	 */
	public abstract String[] getLanguages();

}
