package de.mhus.lib.karaf.services;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.schedule.SchedulerJob;
import de.mhus.lib.core.schedule.TimerTaskIntercepter;

public abstract class SchedulerServiceAdapter extends MLog implements SchedulerService {

	protected boolean canceled = false;

	@Override
	public void onError(Throwable t) {
		
	}

	@Override
	public void onFinal(boolean isError) {
		
	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public String getName() {
		return getClass().getCanonicalName();
	}

	@Override
	public String getInterval() {
		return null;
	}

	@Override
	public SchedulerJob getWrappedJob() {
		return null;
	}

	@Override
	public TimerTaskIntercepter getIntercepter() {
		return null;
	}

}
