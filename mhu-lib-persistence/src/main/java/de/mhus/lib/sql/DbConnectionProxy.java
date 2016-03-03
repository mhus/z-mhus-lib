package de.mhus.lib.sql;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.configupdater.ConfigBoolean;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.parser.Parser;
import de.mhus.lib.core.service.UniqueId;
import de.mhus.lib.errors.MException;

/**
 * The class capsulate the real connection to bring it back into the pool if the connection in no more needed - closed
 * or cleanup by the gc.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DbConnectionProxy extends MObject implements DbConnection {

	private static ConfigBoolean traceCaller = new ConfigBoolean(DbConnection.class, "traceCallers", false);
	
	private DbConnection instance;
	private long id = base(UniqueId.class).nextUniqueId();
//	private StackTraceElement[] createStackTrace;
	private DbPool pool;

	/**
	 * <p>Constructor for DbConnectionProxy.</p>
	 *
	 * @param pool a {@link de.mhus.lib.sql.DbPool} object.
	 * @param instance a {@link de.mhus.lib.sql.DbConnection} object.
	 */
	public DbConnectionProxy(DbPool pool, DbConnection instance) {
		if (traceCaller.value()) {
			this.pool = pool;
			pool.getStackTraces().put(MSystem.getObjectId(this), new ConnectionTrace(this));
//			instance.setUsedTrace(createStackTrace);
		}
		this.instance = instance;
		log().t(id,"created",instance.getInstanceId());
	}

	/** {@inheritDoc} */
	@Override
	public void commit() throws Exception {

		instance.commit();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isReadOnly() throws Exception {
		return instance.isReadOnly();
	}

	/** {@inheritDoc} */
	@Override
	public void rollback() throws Exception {
		instance.rollback();
	}

	/** {@inheritDoc} */
	@Override
	public DbStatement getStatement(String name) throws MException {
		return instance.getStatement(name);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isClosed() {
		if (instance == null) return true;
		return instance.isClosed();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isUsed() {
		return instance.isUsed();
	}

	/** {@inheritDoc} */
	@Override
	public void setUsed(boolean used) {
		if (instance == null) return;
		instance.setUsed(used);
		if (!used) instance  = null; // invalidate this proxy
	}

	/** {@inheritDoc} */
	@Override
	public void close() {
		if (instance == null) return;
		log().t(id,"close",instance.getInstanceId());
		setUsed(false); // close of the proxy will free the connection
		if (traceCaller.value())
			pool.getStackTraces().remove(MSystem.getObjectId(this));
	}

	/** {@inheritDoc} */
	@Override
	protected void finalize() throws Throwable {
		log().t(id,"finalized",instance.getInstanceId());
		if (instance != null) {
			log().i(id,"final closed",
					instance.getInstanceId()
				);
			ConnectionTrace trace = pool.getStackTraces().get(MSystem.getObjectId(this));
			if (trace != null)
				trace.log(log());
			setUsed(false);
		}
		if (traceCaller.value())
			pool.getStackTraces().remove(MSystem.getObjectId(this));
		super.finalize();
	}

	/** {@inheritDoc} */
	@Override
	public DbStatement createStatement(String sql, String language) throws MException {
		return instance.createStatement(sql,language);
	}

	/** {@inheritDoc} */
	@Override
	public long getInstanceId() {
		return id;
	}

	/** {@inheritDoc} */
	@Override
	public Parser createQueryCompiler(String language) throws MException {
		return instance.createQueryCompiler(language);
	}

	/** {@inheritDoc} */
	@Override
	public DbConnection instance() {
		return instance;
	}

	/** {@inheritDoc} */
	@Override
	public DbStatement createStatement(DbPrepared dbPrepared) {
		return instance.createStatement(dbPrepared);
	}

	/** {@inheritDoc} */
	@Override
	public String getDefaultLanguage() {
		return instance.getDefaultLanguage();
	}

	/** {@inheritDoc} */
	@Override
	public String[] getLanguages() {
		return instance.getLanguages();
	}

	/** {@inheritDoc} */
	@Override
	public DbStatement createStatement(String sql) throws MException {
		return instance.createStatement(sql);
	}

}
