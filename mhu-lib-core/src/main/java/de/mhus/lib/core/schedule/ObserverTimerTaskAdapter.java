package de.mhus.lib.core.schedule;

import java.util.Observable;
import java.util.Observer;
import java.util.TimerTask;

import de.mhus.lib.basics.Named;
import de.mhus.lib.core.MTimerTask;
import de.mhus.lib.errors.MRuntimeException;

/**
 * <p>ObserverTimerTaskAdapter class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ObserverTimerTaskAdapter extends MTimerTask implements Observer {

	private TimerTask task;
	/**
	 * <p>Constructor for ObserverTimerTaskAdapter.</p>
	 *
	 * @param task a {@link java.util.TimerTask} object.
	 */
	public ObserverTimerTaskAdapter(TimerTask task) {
		this.task = task;
		if (task != null && task instanceof Named)
			setName(((Named)task).getName());
	}
	
	/** {@inheritDoc} */
	@Override
	public void update(Observable o, Object arg) {
		try {
			doit();
		} catch (Exception e) {
			throw new MRuntimeException(e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return task == null ? "null" : task.toString();
	}

	/** {@inheritDoc} */
	@Override
	public void doit() throws Exception {
		if (task instanceof MTimerTask && ((MTimerTask)task).isCanceled()) {
			cancel();
			return;
		}
			
		task.run();
	}
	
}
