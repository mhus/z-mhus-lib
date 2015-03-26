package de.mhus.lib.core.jms;

import de.mhus.lib.core.IProperties;

public abstract class FunctionDescriptor {

	protected boolean oneWay = false;

	public boolean isOneWay() {
		return oneWay;
	}

	public abstract RequestResult<Object> doExecute(IProperties properties, Object[] obj);

}
