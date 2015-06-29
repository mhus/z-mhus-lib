package de.mhus.lib.sql;

import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.parser.Parser;
import de.mhus.lib.core.service.UniqueId;
import de.mhus.lib.errors.MException;

/**
 * The class capsulate the real connection to bring it back into the pool if the connection in no more needed - closed
 * or cleanup by the gc.
 * 
 * @author mikehummel
 *
 */
public class DbConnectionProxy extends MObject implements DbConnection {

	private DbConnection instance;
	private long id = base(UniqueId.class).nextUniqueId();
	private StackTraceElement[] createStackTrace;

	public DbConnectionProxy(DbConnection instance) {
		if (log().isLocalTrace()) {
			createStackTrace = Thread.currentThread().getStackTrace();
			instance.setUsedTrace(createStackTrace);
		}
		this.instance = instance;
		log().t(id,"created",instance.getInstanceId());
	}

	@Override
	public void commit() throws Exception {

		instance.commit();
	}

	@Override
	public boolean isReadOnly() throws Exception {
		return instance.isReadOnly();
	}

	@Override
	public void rollback() throws Exception {
		instance.rollback();
	}

	@Override
	public DbStatement getStatement(String name) throws MException {
		return instance.getStatement(name);
	}

	@Override
	public boolean isClosed() {
		if (instance == null) return true;
		return instance.isClosed();
	}

	@Override
	public boolean isUsed() {
		return instance.isUsed();
	}

	@Override
	public void setUsed(boolean used) {
		if (instance == null) return;
		instance.setUsed(used);
		if (!used) instance  = null; // invalidate this proxy
	}

	@Override
	public void close() {
		if (instance == null) return;
		log().t(id,"close",instance.getInstanceId());
		setUsed(false); // close of the proxy will free the connection
	}

	@Override
	protected void finalize() throws Throwable {
		log().t(id,"finalized",instance.getInstanceId());
		if (instance != null) {
			log().i(id,"final closed",instance.getInstanceId(),createStackTrace);
			setUsed(false);
		}
		super.finalize();
	}

	@Override
	public DbStatement createStatement(String sql, String language) throws MException {
		return instance.createStatement(sql,language);
	}

	@Override
	public long getInstanceId() {
		return id;
	}

	@Override
	public Parser createQueryCompiler(String language) throws MException {
		return instance.createQueryCompiler(language);
	}

	@Override
	public DbConnection instance() {
		return instance;
	}

	@Override
	public DbStatement createStatement(DbPrepared dbPrepared) {
		return instance.createStatement(dbPrepared);
	}

	@Override
	public void setUsedTrace(StackTraceElement[] createStackTrace) {

	}

	@Override
	public StackTraceElement[] getUsedTrace() {
		return createStackTrace;
	}

	@Override
	public String getDefaultLanguage() {
		return instance.getDefaultLanguage();
	}

	@Override
	public String[] getLanguages() {
		return instance.getLanguages();
	}

	@Override
	public DbStatement createStatement(String sql) throws MException {
		return instance.createStatement(sql);
	}

}
