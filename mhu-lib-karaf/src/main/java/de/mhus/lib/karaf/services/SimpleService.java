package de.mhus.lib.karaf.services;

import de.mhus.lib.core.MLog;

public class SimpleService extends MLog implements SimpleServiceIfc{

	@Override
	public String getSimpleServiceStatus() {
		return "";
	}

	@Override
	public String getSimpleServiceInfo() {
		return getClass().getCanonicalName();
	}

	@Override
	public void doSimpleServiceCommand(String cmd, Object... param) {
	}

}
