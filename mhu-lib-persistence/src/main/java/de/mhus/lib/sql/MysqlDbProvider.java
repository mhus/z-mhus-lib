package de.mhus.lib.sql;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.config.NodeConfig;

/**
 * <p>MysqlDbProvider class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class MysqlDbProvider extends JdbcProvider {

	/**
	 * <p>Constructor for MysqlDbProvider.</p>
	 *
	 * @param host a {@link java.lang.String} object.
	 * @param db a {@link java.lang.String} object.
	 * @param user a {@link java.lang.String} object.
	 * @param pass a {@link java.lang.String} object.
	 */
	public MysqlDbProvider(String host, String db, String user, String pass) {
		this("jdbc:mysql://" + host + (host.indexOf(':') < 0 ? ":3306" : "" ) + "/" + db, user, pass);
	}
	
	/**
	 * <p>Constructor for MysqlDbProvider.</p>
	 *
	 * @param url a {@link java.lang.String} object.
	 * @param user a {@link java.lang.String} object.
	 * @param pass a {@link java.lang.String} object.
	 */
	public MysqlDbProvider(String url, String user, String pass) {
		config = new NodeConfig();
		config.setProperty("driver", "com.mysql.jdbc.Driver");
		config.setProperty("url", url );
		config.setProperty("user", user);
		config.setProperty("pass", pass);
		config.setProperty("name", url);
		activator = base(MActivator.class);
	}
		
}
