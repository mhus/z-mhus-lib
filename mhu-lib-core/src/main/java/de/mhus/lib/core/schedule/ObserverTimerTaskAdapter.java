package de.mhus.lib.core.schedule;

import java.util.Observable;
import java.util.Observer;
import java.util.TimerTask;

public class ObserverTimerTaskAdapter implements Observer {

	private TimerTask task;
	public ObserverTimerTaskAdapter(TimerTask task) {
		this.task = task;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		task.run();
	}

}
