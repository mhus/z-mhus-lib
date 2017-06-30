package de.mhus.lib.core.jmx;

import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.core.MStopWatch;

@JmxManaged(descrition="Simple Stop Watch")
public class JmxStopWatch extends MStopWatch {

	@Override
	@JmxManaged(descrition="Current status of the watch")
	public String getStatusAsString() {
		return super.getStatusAsString();
	}
	
	@Override
	@JmxManaged(descrition="Currently elapsed time")
	public String getCurrentTimeAsString() {
		return super.getCurrentTimeAsString();
	}
	
	@Override
	@JmxManaged(descrition="Name of the watch")
	public String getName() {
		return super.getName();
	}
	
}
