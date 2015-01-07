package de.mhus.lib.sql;

import java.io.IOException;
import java.sql.Connection;

import de.mhus.lib.core.parser.Parser;
import de.mhus.lib.core.service.UniqueId;
import de.mhus.lib.errors.MException;
import de.mhus.lib.sql.parser.SimpleQueryCompiler;

/**
 * This class is a proxy for DbProvider and it is the public interface for it. It will automatic free
 * the provider in the pool if the connection is no more needed.
 * 
 * @author mikehummel
 *
 */
public class JdbcConnection extends InternalDbConnection {
	
	public static final String LANGUAGE_SQL = "sql";
	
	private boolean used = false;
	private Connection connection;
	private DbProvider provider;
	private boolean closed;

	private long id;

	private StackTraceElement[] createStackTrace;


	@Override
	public void commit() throws Exception {
		log().t(poolId,id,"commit");
		if (closed) throw new MException(poolId,id,"Connection not valid");
		if (!connection.getAutoCommit())
			connection.commit();
	}

	@Override
	public boolean isReadOnly() throws Exception {
		if (closed) throw new MException(poolId,id,"Connection not valid");
		return connection.isReadOnly();
	}

	@Override
	public void rollback() throws Exception {
		log().t(poolId,id,"rollback");
		if (closed) throw new IOException("Connection not valid");
		connection.rollback();
	}

	public JdbcConnection(DbProvider provider,Connection con) {
		this.provider=provider;
		this.connection = con;
		id = base(UniqueId.class).nextUniqueId();
	}
	
	@Override
	public DbStatement getStatement(String name) throws MException {
		synchronized (this) {
			if (closed) throw new MException("Connection not valid");
			
			String[] query = provider.getQuery(name);
			if (query == null) return null;
			return new JdbcStatement(this, query[1],query[0]);
		}
	}
		
	@Override
	public DbStatement createStatement(String sql, String language) throws MException {
		synchronized (this) {
			if (closed) throw new MException("Connection not valid");
			
			return new JdbcStatement(this, sql, language);
		}
	}
	
	@Override
	public boolean isClosed() {
		synchronized (this) {
			return closed;
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
			close();
		super.finalize();
	}
	
	@Override
	public boolean isUsed() {
		synchronized (this) {
			return used ;
		}
	}

	@Override
	public void setUsed(boolean used) {
		log().t(poolId,id,"used",used);
		super.setUsed(used);
		synchronized (this) {
			this.used = used;
			if (!used) // for security reasons - remove old garbage in the session
				try {
					rollback();
				} catch (Exception e) {
					log().d(e);
					close();
				}
		}
	}

	/**
	 * Returns the JDBC Connection - if possible.
	 * 
	 * @return JDBC Connection or null
	 */
	public Connection getConnection() {
		return connection;
	}
	

	@Override
	public void close() {
		log().t(poolId,id,"close");
		synchronized (this) {
			try {
				if (connection!=null && !connection.isClosed()) {
					connection.close();
					connection = null;
				}
			} catch (Throwable e) {
				log().d(this,e);
				connection = null;
			}
			closed  = true;
		}
	}

	@Override
	public long getInstanceId() {
		return id;
	}

	@Override
	public Parser createQueryCompiler(String language) throws MException {
		if (pool != null) return pool.getDialect().getQueryParser(language);
		return new SimpleQueryCompiler();
	}

	@Override
	public DbConnection instance() {
		return this;
	}

	@Override
	public DbStatement createStatement(DbPrepared dbPrepared) {
		return new JdbcStatement(this, dbPrepared);
	}

	@Override
	public void setUsedTrace(StackTraceElement[] createStackTrace) {
		this.createStackTrace = createStackTrace;
	}

	@Override
	public StackTraceElement[] getUsedTrace() {
		return createStackTrace;
	}

	public void setTimeoutUnused(long timeoutUnused) {
		this.timeoutUnused = timeoutUnused;
	}

	public void setTimeoutLifetime(long timeoutLifetime) {
		this.timeoutLifetime = timeoutLifetime;
	}

	@Override
	public String getDefaultLanguage() {
		return LANGUAGE_SQL;
	}

	@Override
	public String[] getLanguages() {
		return new String[] {LANGUAGE_SQL};
	}

	@Override
	public DbStatement createStatement(String sql) throws MException {
		return createStatement(sql, provider.getDialect().detectLanguage(sql));
	}	

}
