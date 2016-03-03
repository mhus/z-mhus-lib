package de.mhus.lib.sql;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.configupdater.ConfigBoolean;
import de.mhus.lib.core.configupdater.ConfigLong;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.core.parser.CompiledString;
import de.mhus.lib.errors.MException;

/**
 * This represents a qyery statement. Use it to execute queries.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class JdbcStatement extends DbStatement {


	private static ConfigBoolean traceRuntime = new ConfigBoolean(DbConnection.class, "traceRuntime", false);
	private static ConfigLong traceMaxRuntime = new ConfigLong(DbConnection.class, "traceMaxRuntime", MTimeInterval.MINUTE_IN_MILLISECOUNDS);
	
	private JdbcConnection dbCon;
	private Statement sth;

	private CompiledString query;

	private PreparedStatement preparedSth;

	private String xquery;

	JdbcStatement(JdbcConnection dbCon, DbPrepared prepared) {
		this.dbCon = dbCon;
		this.query = prepared.getQuery();
	}

	JdbcStatement(JdbcConnection dbCon, String query,String language) throws MException {
		this.dbCon = dbCon;
		this.query = dbCon.createQueryCompiler(language).compileString(query);
	}

	private void validateSth() throws Exception {
		synchronized (this) {
			if (sth==null || sth.isClosed()) {
				Connection con = dbCon.getConnection();
				sth = con.createStatement();
			}
		}
	}

	/**
	 * <p>prepareStatement.</p>
	 *
	 * @param attributes a {@link java.util.Map} object.
	 * @param sth a {@link java.sql.Statement} object.
	 * @param query a {@link java.lang.String} object.
	 * @return a {@link java.sql.PreparedStatement} object.
	 * @throws java.sql.SQLException if any.
	 */
	protected PreparedStatement prepareStatement(Map<String, Object> attributes, Statement sth, String query ) throws SQLException {

		// recycle prepared query - should not differ !
		if (xquery != null & preparedSth != null && xquery.equals(query))
			return preparedSth;

		// if differ close last prepared query
		closePreparedSth();

		// checkout new
		if (attributes != null && attributes.containsKey( RETURN_BINARY_KEY + "0")) {
			PreparedStatement psth = dbCon.getConnection().prepareStatement(query);
			xquery = query;
			for (int nr = 0; attributes.containsKey(RETURN_BINARY_KEY + nr); nr++) {
				psth.setBinaryStream(nr+1, (InputStream) attributes.get(RETURN_BINARY_KEY + nr));
				attributes.remove(RETURN_BINARY_KEY + nr);
			}
			return psth;
		}
		return null;
	}

	/**
	 * <p>closePreparedSth.</p>
	 */
	protected void closePreparedSth() {
		if (preparedSth != null) {
			xquery = null;
			try {
				preparedSth.close();
			} catch (SQLException e) {
				log().t(e);
			}
			preparedSth = null;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * Executes the given SQL statement, which may return multiple results. In this statement
	 * InputStream as attribute values are allowed.
	 */
	@Override
	public boolean execute(Map<String, Object> attributes) throws Exception {
		validateSth();
		String query = this.query.execute(attributes);
		log().t(query);
		try {
			preparedSth = prepareStatement(attributes, sth, query);
			long start = System.currentTimeMillis();
			boolean result = preparedSth == null ? sth.execute(query) : preparedSth.execute();
			trace(query,start);
			return result;
		} catch (Exception e) {
			log().e(query);
			throw e;
		}
	}

	/** {@inheritDoc} */
	@Override
	public DbResult getResultSet() throws SQLException {
		return new JdbcResult(this, sth.getResultSet());
	}

	/** {@inheritDoc} */
	@Override
	public int getUpdateCount() throws SQLException {
		return sth.getUpdateCount();
	}

	/**
	 * {@inheritDoc}
	 *
	 * Return the result of an select query.
	 */
	@Override
	public DbResult executeQuery(Map<String, Object> attributes) throws Exception {
		validateSth();
		String query = this.query.execute(attributes);
		log().t(query);
		preparedSth = prepareStatement(attributes, sth, query);
		long start = System.currentTimeMillis();
		ResultSet result = preparedSth == null ? sth.executeQuery(query) : preparedSth.executeQuery();
		trace(query,start);
		return new JdbcResult(this, result );
	}

	/**
	 * {@inheritDoc}
	 *
	 * Return the result of an update query. In the attributes InputStreams are allowed (blobs).
	 */
	@Override
	public int executeUpdate(Map<String, Object> attributes) throws Exception {
		validateSth();
		String query = this.query.execute(attributes);
		log().t(query);
		preparedSth = prepareStatement(attributes, sth, query);
		long start = System.currentTimeMillis();
		int result = preparedSth == null ? sth.executeUpdate(query) : preparedSth.executeUpdate();
		trace(query,start);
		return result;
	}

	/**
	 * <p>trace.</p>
	 *
	 * @param query a {@link java.lang.String} object.
	 * @param start a long.
	 * @since 3.2.9
	 */
	protected void trace(String query, long start) {
		if (!traceRuntime.value()) return;
		long stop = System.currentTimeMillis();
		if (stop - start > traceMaxRuntime.value()) {
			log().f(getConnection().getInstanceId(),"Query Runtime Warning",stop-start,query);
			MLogUtil.logStackTrace(log(), ""+getConnection().getInstanceId(), Thread.currentThread().getStackTrace());
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * Return the used connection.
	 */
	@Override
	public DbConnection getConnection() {
		return dbCon;
	}

	/** {@inheritDoc} */
	@Override
	public void close() {
		closePreparedSth();
		if (sth == null) return;
		try {
			if (sth!=null && !sth.isClosed()) {
				sth.close();
			}
		} catch (Exception e) {
			log().i(e);
		}
		sth = null;
	}

}
