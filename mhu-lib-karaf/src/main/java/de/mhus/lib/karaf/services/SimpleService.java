package de.mhus.lib.karaf.services;

import de.mhus.lib.core.MLog;

public class SimpleService extends MLog implements SimpleServiceIfc{

	@Override
	public String getStatus() {
		return "";
	}

	@Override
	public void setConfiguration(String in) {
	}

}
