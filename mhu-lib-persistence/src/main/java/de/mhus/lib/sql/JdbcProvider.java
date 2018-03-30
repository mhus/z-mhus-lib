/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.sql;

import java.sql.Connection;
import java.sql.DriverManager;

import de.mhus.lib.core.MPassword;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MTimeInterval;

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
					else
					if (driver.indexOf("postgresql") > 0)
						dialect = new DialectPostgresql();
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
			if (activator != null)
				activator.getClazz(driver);
			else
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
