/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.jms;

import de.mhus.lib.core.IProperties;

public class ServerJsonProxy<T> extends ServerJsonObject implements JmsObjectProxy {

	private ServiceDescriptor service;

	public ServerJsonProxy(JmsDestination dest, ServiceDescriptor service) {
		super(dest);
		this.service = service;
	}

	@Override
	public void receivedOneWay(IProperties properties, Object... obj) {
		String functionName = properties.getString("function", null);
		if (functionName == null) {
			log().w("function not set",getJmsDestination());
			return;
		}
		functionName = functionName.toLowerCase();
		FunctionDescriptor function = service.getFunction(functionName);
		if (function == null) {
			log().w("function not found",functionName,getJmsDestination());
			return;
		}
		if (!function.isOneWay()) {
			log().w("function not one way",functionName,getJmsDestination());
			return;
		}
		function.doExecute(properties, obj);
	}

	@Override
	public RequestResult<Object> received(IProperties properties, Object... obj) {
		String functionName = properties.getString("function", null);
		if (functionName == null) {
			log().w("function not set",getJmsDestination());
			return null;
		}
		functionName = functionName.toLowerCase();
		FunctionDescriptor function = service.getFunction(functionName);
		if (function == null) {
			log().w("function not found",functionName,getJmsDestination());
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
