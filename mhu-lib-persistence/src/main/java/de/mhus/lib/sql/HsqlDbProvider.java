package de.mhus.lib.sql;

import java.util.UUID;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.config.NodeConfig;

public class HsqlDbProvider extends JdbcProvider {

	public HsqlDbProvider() {
		this(UUID.randomUUID().toString());
	}
	
	public HsqlDbProvider(String memoryDbName) {
		config = new NodeConfig();
		config.setProperty("driver", "org.hsqldb.jdbcDriver");
		config.setProperty("url", "jdbc:hsqldb:mem:" + memoryDbName);
		config.setProperty("user", "sa");
		config.setProperty("pass", "");
		config.setProperty("name", memoryDbName);
		activator = base(MActivator.class);
	}
	
	public HsqlDbProvider(String file, String user, String pass) {
		config = new NodeConfig();
		config.setProperty("driver", "org.hsqldb.jdbcDriver");
		config.setProperty("url", "file:" + file);
		config.setProperty("user", user);
		config.setProperty("pass", pass);
		config.setProperty("name", file);
		activator = base(MActivator.class);
	}
	
}
