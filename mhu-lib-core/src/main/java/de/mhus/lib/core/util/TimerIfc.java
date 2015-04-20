package de.mhus.lib.core.util;

import java.util.Date;
import java.util.TimerTask;

import de.mhus.lib.annotations.activator.DefaultFactory;

@DefaultFactory(DefaultTimerFactory.class)
public interface TimerIfc {

	public void schedule(TimerTask task, long delay);

	public void schedule(TimerTask task, Date time);

	public void schedule(TimerTask task, long delay, long period);

	public void schedule(TimerTask task, Date firstTime, long period);

	public void scheduleAtFixedRate(TimerTask task, long delay, long period);

	public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period);

	public void cancel();

}
