package de.mhus.lib.sql;

import java.util.UUID;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.config.NodeConfig;

/**
 * <p>HsqlDbProvider class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class HsqlDbProvider extends JdbcProvider {

	/**
	 * <p>Constructor for HsqlDbProvider.</p>
	 */
	public HsqlDbProvider() {
		this(UUID.randomUUID().toString());
	}
	
	/**
	 * <p>Constructor for HsqlDbProvider.</p>
	 *
	 * @param memoryDbName a {@link java.lang.String} object.
	 */
	public HsqlDbProvider(String memoryDbName) {
		config = new NodeConfig();
		config.setProperty("driver", "org.hsqldb.jdbcDriver");
		config.setProperty("url", "jdbc:hsqldb:mem:" + memoryDbName);
		config.setProperty("user", "sa");
		config.setProperty("pass", "");
		config.setProperty("name", memoryDbName);
		activator = MApi.lookup(MActivator.class);
	}
	
	/**
	 * <p>Constructor for HsqlDbProvider.</p>
	 *
	 * @param file a {@link java.lang.String} object.
	 * @param user a {@link java.lang.String} object.
	 * @param pass a {@link java.lang.String} object.
	 */
	public HsqlDbProvider(String file, String user, String pass) {
		config = new NodeConfig();
		config.setProperty("driver", "org.hsqldb.jdbcDriver");
		config.setProperty("url", "file:" + file);
		config.setProperty("user", user);
		config.setProperty("pass", pass);
		config.setProperty("name", file);
		activator = MApi.lookup(MActivator.class);
	}
	
}
