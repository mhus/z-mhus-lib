package de.mhus.lib.core.util;

import de.mhus.lib.annotations.activator.DefaultImplementation;

@DefaultImplementation(DefaultTimerFactory.class)
public interface TimerFactory {

	public TimerIfc getTimer();
}
