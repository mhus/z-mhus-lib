package de.mhus.lib.core;

import de.mhus.lib.core.logging.Log;

/**
 * <p>Abstract MHousekeeperTask class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class MHousekeeperTask extends MTimerTask {
	
	private Log log = Log.getLog(this.getClass());

	/**
	 * <p>log.</p>
	 *
	 * @return a {@link de.mhus.lib.core.logging.Log} object.
	 */
	protected Log log() {
		return log;
	}
}
