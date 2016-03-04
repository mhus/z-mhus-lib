package de.mhus.lib.jms;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.IProperties;

public abstract class FunctionDescriptor {

	protected boolean oneWay = false;
	protected Class<?> returnType = Void.class;

	public boolean isOneWay() {
		return oneWay;
	}

	public abstract RequestResult<Object> doExecute(IProperties properties, Object[] obj);

	public Class<?> getReturnType() {
		return returnType;
	}
	
}
