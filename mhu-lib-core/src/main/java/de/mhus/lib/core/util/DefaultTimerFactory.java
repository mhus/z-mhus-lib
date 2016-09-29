package de.mhus.lib.core.util;

import java.util.Timer;

import de.mhus.lib.annotations.activator.ObjectFactory;

/**
 * <p>DefaultTimerFactory class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DefaultTimerFactory implements TimerFactory, ObjectFactory {

	private Timer timer;
	
	/** {@inheritDoc} */
	@Override
	public TimerIfc getTimer() {
		init();
		return new TimerImpl(timer);
	}

	/** {@inheritDoc} */
	@Override
	public Object create(Class<?> clazz, Class<?>[] classes, Object[] objects) {
		init();
		return new TimerImpl(timer);
	}

	private synchronized void init() {
		if (timer == null)
			timer = new Timer(TimerIfc.class.getCanonicalName(), true);
	}
	
}
