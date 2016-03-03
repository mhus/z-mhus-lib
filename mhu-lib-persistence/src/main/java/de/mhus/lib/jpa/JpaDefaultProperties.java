package de.mhus.lib.jpa;

import java.util.Properties;

import de.mhus.lib.core.directory.ResourceNode;

/**
 * <p>JpaDefaultProperties class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class JpaDefaultProperties extends JpaProperties {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for JpaDefaultProperties.</p>
	 */
	public JpaDefaultProperties() {
		super();
	}

	/**
	 * <p>Constructor for JpaDefaultProperties.</p>
	 *
	 * @param config a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 */
	public JpaDefaultProperties(ResourceNode config) {
		super(config);
	}

	/**
	 * <p>Constructor for JpaDefaultProperties.</p>
	 *
	 * @param arg0 a {@link java.util.Properties} object.
	 */
	public JpaDefaultProperties(Properties arg0) {
		super(arg0);
	}

	/**
	 * <p>configureMysql.</p>
	 *
	 * @param url a {@link java.lang.String} object.
	 * @param user a {@link java.lang.String} object.
	 * @param password a {@link java.lang.String} object.
	 */
	public void configureMysql(String url, String user, String password) {
		setProperty("openjpa.ConnectionURL", url);
		setProperty("openjpa.ConnectionDriverName", "com.mysql.jdbc.Driver");
		setProperty("openjpa.ConnectionUserName", user);
		setProperty("openjpa.ConnectionPassword", password);
	}

	/**
	 * <p>configureHsqlMemory.</p>
	 */
	public void configureHsqlMemory() {
		setProperty("openjpa.ConnectionDriverName", "org.hsqldb.jdbcDriver");
		setProperty("openjpa.ConnectionURL", "jdbc:hsqldb:mem:aname");
		setProperty("openjpa.ConnectionUserName", "sa");
		setProperty("openjpa.ConnectionPassword", "");
	}

	/**
	 * <p>enableSqlTrace.</p>
	 */
	public void enableSqlTrace() {
		setProperty("openjpa.Log", "SQL=TRACE");
	}

}
