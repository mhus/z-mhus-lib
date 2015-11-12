package de.mhus.lib.logging.auris;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MLog;

public class AurisSender extends MLog {
	protected int port;
	protected String host;
	protected String name;

	public AurisSender(IProperties config) {
		port = config.getInt("port", 2030);
		host = config.getString("host", "localhost");
		name = config.getString("name",host + ":" + port);
	}

}
