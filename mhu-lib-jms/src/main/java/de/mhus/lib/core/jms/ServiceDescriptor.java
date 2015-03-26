package de.mhus.lib.core.jms;

import java.util.HashMap;

import de.mhus.lib.core.MLog;

public class ServiceDescriptor extends MLog {
	
	protected HashMap<String, FunctionDescriptor> functions = new HashMap<>();
	protected Class<?> ifc;
	
	public ServiceDescriptor(Class<?> ifc) {
		this.ifc = ifc;
	}
	
	public FunctionDescriptor getFunction(String name) {
		return functions.get(name);
	}

	public Class<?>getInterface() {
		return ifc;
	}

}
