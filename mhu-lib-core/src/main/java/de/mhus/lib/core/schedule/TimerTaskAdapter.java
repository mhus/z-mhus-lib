package de.mhus.lib.core.schedule;

import java.util.Observable;
import java.util.Observer;
import java.util.TimerTask;

import de.mhus.lib.basics.Named;
import de.mhus.lib.core.ITimerTask;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MTimerTask;
import de.mhus.lib.errors.MRuntimeException;

public class TimerTaskAdapter extends MTimerTask implements Observer {

	private TimerTask task;
	
	public TimerTaskAdapter(TimerTask task) {
		this(null,task);
	}
	
	public TimerTaskAdapter(String name, TimerTask task) {
		this.task = task;
		if (name == null) name = MSystem.getClassName(task);
		setName(name);
		if (task != null && task instanceof Named)
			setName(((Named)task).getName());
	}
	
	@Override
	public void update(Observable o, Object arg) {
		try {
			doit();
		} catch (Exception e) {
			throw new MRuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return task == null ? "null" : task.toString();
	}

	@Override
	public void doit() throws Exception {
		if (task instanceof ITimerTask && ((ITimerTask)task).isCanceled()) {
			cancel();
			return;
		}
			
		task.run();
	}

	public TimerTask getTask() {
		return task;
	}
	
}
