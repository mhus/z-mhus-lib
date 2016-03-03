package de.mhus.lib.core.util;

import de.mhus.lib.annotations.activator.DefaultImplementation;

/**
 * <p>TimerFactory interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
@DefaultImplementation(DefaultTimerFactory.class)
public interface TimerFactory {

	/**
	 * <p>getTimer.</p>
	 *
	 * @return a {@link de.mhus.lib.core.util.TimerIfc} object.
	 */
	public TimerIfc getTimer();
}
