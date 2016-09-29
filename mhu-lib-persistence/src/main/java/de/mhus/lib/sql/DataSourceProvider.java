package de.mhus.lib.sql;

import java.sql.Connection;

import javax.sql.DataSource;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.directory.ResourceNode;

public class DataSourceProvider extends DbProvider {

	private DataSource dataSource;
	private Dialect dialect;

	public DataSourceProvider() {}

	public DataSourceProvider(DataSource dataSource, Dialect dialect, ResourceNode config, MActivator activator) {
		doInitialize(config, activator);
		setDataSource(dataSource);
		setDialect(dialect);
	}

	@Override
	public InternalDbConnection createConnection() throws Exception {
		if (dataSource == null) return null;
		Connection con = null;
		try {
			con = dataSource.getConnection();
		} catch (Exception e) {
			// special behavior for e.g. mysql, retry to get a connection after gc()
			// Caused by: com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException: Too many connections
			if (e.getMessage().indexOf("Too many connections") > -1) {
				System.gc();
				con = dataSource.getConnection();
			} else
				throw e;
		}
		getDialect().prepareConnection(con);
		return new JdbcConnection(this, con);
	}

	@Override
	public Dialect getDialect() {
		if (dialect == null)
			// default is mysql, can ignore sync!
			dialect = new DialectMysql();
		return dialect;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

}
