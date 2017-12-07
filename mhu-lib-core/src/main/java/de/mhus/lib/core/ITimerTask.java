package de.mhus.lib.core;

import de.mhus.lib.basics.Named;

public interface ITimerTask extends Named {

	void run(Object environment);

	void onError(Throwable t);

	void onFinal(boolean isError);
	
	boolean isCanceled();
	
}
