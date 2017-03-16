package de.mhus.lib.sql;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.config.NodeConfig;

public class MysqlDbProvider extends JdbcProvider {

	public MysqlDbProvider(String host, String db, String user, String pass) {
		this("jdbc:mysql://" + host + (host.indexOf(':') < 0 ? ":3306" : "" ) + "/" + db, user, pass);
	}
	
	public MysqlDbProvider(String url, String user, String pass) {
		config = new NodeConfig();
		config.setProperty("driver", "com.mysql.jdbc.Driver");
		config.setProperty("url", url );
		config.setProperty("user", user);
		config.setProperty("pass", pass);
		config.setProperty("name", url);
		activator = MApi.lookup(MActivator.class);
	}
		
}
