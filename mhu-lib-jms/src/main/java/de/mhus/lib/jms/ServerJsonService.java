package de.mhus.lib.jms;

import de.mhus.lib.core.AbstractProperties;

public class ServerJsonService<T> extends ServerJsonObject implements JmsChannelService {

	private ServiceDescriptor service;

	public ServerJsonService(JmsDestination dest, ServiceDescriptor service) {
		super(dest);
		this.service = service;
	}

	@Override
	public void receivedOneWay(AbstractProperties properties, Object... obj) {
		String functionName = properties.getString("function", null);
		if (functionName == null) {
			log().w("function not set",getDestination());
			return;
		}
		functionName = functionName.toLowerCase();
		FunctionDescriptor function = service.getFunction(functionName);
		if (function == null) {
			log().w("function not found",functionName,getDestination());
			return;
		}
		if (!function.isOneWay()) {
			log().w("function not one way",functionName,getDestination());
			return;
		}
		function.doExecute(properties, obj);
	}

	@Override
	public RequestResult<Object> received(AbstractProperties properties, Object... obj) {
		String functionName = properties.getString("function", null);
		if (functionName == null) {
			log().w("function not set",getDestination());
			return null;
		}
		functionName = functionName.toLowerCase();
		FunctionDescriptor function = service.getFunction(functionName);
		if (function == null) {
			log().w("function not found",functionName,getDestination());
			return null;
		}
		return function.doExecute(properties, obj);
	}

	@Override
	public Class<?> getInterface() {
		return service.getInterface();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getObject() {
		return (T)service.getObject();
	}

}
