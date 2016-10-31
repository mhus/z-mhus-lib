package de.mhus.lib.jpa;

import java.util.Properties;

import de.mhus.lib.core.config.IConfig;

public class JpaDefaultProperties extends JpaProperties {

	private static final long serialVersionUID = 1L;

	public JpaDefaultProperties() {
		super();
	}

	public JpaDefaultProperties(IConfig config) {
		super(config);
	}

	public JpaDefaultProperties(Properties arg0) {
		super(arg0);
	}

	public void configureMysql(String url, String user, String password) {
		setProperty("openjpa.ConnectionURL", url);
		setProperty("openjpa.ConnectionDriverName", "com.mysql.jdbc.Driver");
		setProperty("openjpa.ConnectionUserName", user);
		setProperty("openjpa.ConnectionPassword", password);
	}

	public void configureHsqlMemory() {
		setProperty("openjpa.ConnectionDriverName", "org.hsqldb.jdbcDriver");
		setProperty("openjpa.ConnectionURL", "jdbc:hsqldb:mem:aname");
		setProperty("openjpa.ConnectionUserName", "sa");
		setProperty("openjpa.ConnectionPassword", "");
	}

	public void enableSqlTrace() {
		setProperty("openjpa.Log", "SQL=TRACE");
	}

}
