package de.mhus.lib.core.cfg;

import de.mhus.lib.core.config.IConfig;

/**
 * <p>CfgProvider interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public interface CfgProvider {

	/**
	 * <p>getConfig.</p>
	 *
	 * @return a {@link de.mhus.lib.core.config.IConfig} object.
	 */
	public IConfig getConfig();
	
	/**
	 * <p>doStart.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void doStart(String name);
	/**
	 * <p>doStop.</p>
	 */
	public void doStop();
	
}
