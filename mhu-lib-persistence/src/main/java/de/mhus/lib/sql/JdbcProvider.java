package de.mhus.lib.sql;

import java.sql.Connection;
import java.sql.DriverManager;

import de.mhus.lib.core.MPassword;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;

/**
 * Database provider for the jdbc database access variant.
 * 
 * @author mikehummel
 *
 */
public class JdbcProvider extends DbProvider {

	private Dialect dialect;

	@Override
	public synchronized Dialect getDialect() {
		if (dialect == null) {
			// ResourceNode concon = config.getNode("connection");
			String dialectName = config.getExtracted("dialect");
			if (dialect != null) {
				try {
					dialect = (Dialect)activator.getObject(dialectName);
				} catch (Exception e) {
					log().t(dialect,e);
				}
			}
			if (dialect == null) {
				String driver = config.getExtracted("driver");
				if (driver != null) {
					if (driver.indexOf("hsqldb") > 0)
						dialect = new DialectHsqldb();
					else
						if (driver.indexOf("mysql") > 0)
							dialect = new DialectMysql();
				}
			}
			if (dialect == null) {
				dialect = new DialectDefault();
			}
			log().t("dialect",dialect.getClass().getCanonicalName());
		}
		return dialect;
	}

	@Override
	public InternalDbConnection createConnection() throws Exception {
		// ResourceNode concon = config.getNode("connection");
		String driver = config.getExtracted("driver");
		String url = config.getExtracted("url");
		String user = config.getExtracted("user");
		String pass = config.getExtracted("pass");

		if (!MString.isEmpty(driver)) {
			//			activator.getClazz(driver);
			Class.forName(driver);
		}

		log().t(driver,url,user);
		Connection con = DriverManager.getConnection(url,user,MPassword.decode(pass));
		getDialect().prepareConnection(con);
		JdbcConnection dbCon = new JdbcConnection(this,con);
		long timeoutUnused = MTimeInterval.toMilliseconds( config.getExtracted("timeout_unused"), 0 );
		long timeoutLifetime = MTimeInterval.toMilliseconds( config.getExtracted("timeout_lifetime"), 0 );
		if (timeoutUnused  > 0) dbCon.setTimeoutUnused(timeoutUnused);
		if (timeoutLifetime > 0) dbCon.setTimeoutLifetime(timeoutLifetime);
		return dbCon;
	}

}
