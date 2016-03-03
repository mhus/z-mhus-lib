package de.mhus.lib.logging.auris;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MLog;

/**
 * <p>AurisSender class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class AurisSender extends MLog {
	protected int port;
	protected String host;
	protected String name;

	/**
	 * <p>Constructor for AurisSender.</p>
	 *
	 * @param config a {@link de.mhus.lib.core.IProperties} object.
	 */
	public AurisSender(IProperties config) {
		port = config.getInt("port", 2030);
		host = config.getString("host", "localhost");
		name = config.getString("name",host + ":" + port);
	}

}
