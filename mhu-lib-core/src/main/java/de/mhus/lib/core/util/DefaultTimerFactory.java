package de.mhus.lib.core.util;

import java.util.Timer;

import de.mhus.lib.annotations.activator.ObjectFactory;
import de.mhus.lib.core.base.service.TimerFactory;
import de.mhus.lib.core.base.service.TimerIfc;
import de.mhus.lib.core.base.service.TimerImpl;

public class DefaultTimerFactory implements TimerFactory, ObjectFactory {

	private Timer timer;
	
	@Override
	public TimerIfc getTimer() {
		init();
		return new TimerImpl(timer);
	}

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
