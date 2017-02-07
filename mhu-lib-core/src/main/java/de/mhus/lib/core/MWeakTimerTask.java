package de.mhus.lib.core;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Observable;
import java.util.Observer;
import java.util.TimerTask;

import de.mhus.lib.basics.Named;

public class MWeakTimerTask extends TimerTask implements Observer, Named {

	private boolean canceled = false;
	private String name;
	private WeakReference<TimerTask> task;
	
	public MWeakTimerTask(TimerTask task) {
		this.task = new WeakReference<>(task);
		setName(MSystem.getClassName(this));
	}
		
	@Override
	final public void run() {
		boolean error = false;
		try {
			doit();
		} catch (Throwable t) {
			try {
				onError(t);
			} catch (Throwable t1) {
			}
			error = true;
		}
		try {
			onFinal(error);
		} catch (Throwable t) {
		}
	}

	protected void onError(Throwable t) {
		t.printStackTrace();
	}

	protected void onFinal(boolean isError) {
	}

	protected void doit() throws Exception {
		if (task == null) {
			cancel();
			return;
		}
		TimerTask taskTask = task.get();
		if (taskTask == null || MTimerTask.getStatus(taskTask) == MTimerTask.CANCELLED ) {
			cancel();
			return;
		}
		
		taskTask.run();
	}
	
    @Override
	public void update(Observable o, Object arg) {
    	run();
    }

    @Override
    public boolean cancel() {
    	setCanceled(true);
    	return super.cancel();
    }
    
	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
