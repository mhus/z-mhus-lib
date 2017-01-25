package de.mhus.lib.core.base.service;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.util.DefaultTimerFactory;

@DefaultImplementation(DefaultTimerFactory.class)
public interface TimerFactory {

	public TimerIfc getTimer();
}
