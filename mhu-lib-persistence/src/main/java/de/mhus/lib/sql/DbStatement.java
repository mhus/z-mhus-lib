package de.mhus.lib.sql;

import java.sql.SQLException;
import java.util.Map;

import de.mhus.lib.core.lang.MObject;

/**
 * This represents a qyery statement. Use it to execute queries.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class DbStatement extends MObject {

	/** Constant <code>RETURN_BINARY_KEY="return_binary_attribute_"</code> */
	public static final String RETURN_BINARY_KEY = "return_binary_attribute_";

	/** {@inheritDoc} */
	@Override
	protected void finalize() throws Throwable {
		close();
	};

	/**
	 * Executes the given SQL statement, which may return multiple results. In this statement
	 * InputStream as attribute values are allowed.
	 *
	 * @param attributes a {@link java.util.Map} object.
	 * @throws java.lang.Exception if any.
	 * @return a boolean.
	 */
	public abstract boolean execute(Map<String, Object> attributes) throws Exception;

	/**
	 * <p>getResultSet.</p>
	 *
	 * @return a {@link de.mhus.lib.sql.DbResult} object.
	 * @throws java.sql.SQLException if any.
	 */
	public abstract DbResult getResultSet() throws SQLException;

	/**
	 * <p>getUpdateCount.</p>
	 *
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	public abstract int getUpdateCount() throws SQLException;

	/**
	 * Return the result of an select query.
	 *
	 * @param attributes a {@link java.util.Map} object.
	 * @throws java.lang.Exception if any.
	 * @return a {@link de.mhus.lib.sql.DbResult} object.
	 */
	public abstract DbResult executeQuery(Map<String, Object> attributes) throws Exception;

	/**
	 * Return the result of an update query. In the attributes InputStreams are allowed (blobs).
	 *
	 * @param attributes a {@link java.util.Map} object.
	 * @throws java.lang.Exception if any.
	 * @return a int.
	 */
	public abstract int executeUpdate(Map<String, Object> attributes) throws Exception;

	/**
	 * Return the used connection.
	 *
	 * @return a {@link de.mhus.lib.sql.DbConnection} object.
	 */
	public abstract DbConnection getConnection();

	/**
	 * Close the statement and free resources.
	 */
	public abstract void close();

	/**
	 * This is a small hack to add binaries (BLOB) to the statement. The blob is stored in the attributes and will
	 * e used in execute time to be added to the statement. See JdbcStatement.
	 *
	 * @param attributes a {@link java.util.Map} object.
	 * @param value a {@link java.lang.Object} object.
	 */
	public static void addBinary(Map<String, Object> attributes, Object value) {
		int nr = 0;
		while (attributes.containsKey(DbStatement.RETURN_BINARY_KEY + nr)) nr++;
		attributes.put(DbStatement.RETURN_BINARY_KEY + nr, value);
	}

}
