package de.mhus.lib.core.jms;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.errors.MRuntimeException;

public class ClientJsonService<T> extends ClientJsonObject {

	protected T proxy;
	protected ServiceDescriptor desc;

	public ClientJsonService(JmsDestination dest, ServiceDescriptor desc ) {
		super(dest);
		this.desc = desc;
		createProxy();
	}

	@SuppressWarnings("unchecked")
	protected void createProxy() {
		Class<?> ifc = desc.getInterface();
		if (ifc == null) return;
		
	    InvocationHandler handler = new MyInvocationHandler();

		proxy = (T) Proxy.newProxyInstance(ifc.getClassLoader(),
                new Class[] { ifc },
                handler);
		
	}

	public T getClientProxy() {
		return proxy;
	}
	
	private class MyInvocationHandler implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {

			String name = method.getName().toLowerCase();
			
			FunctionDescriptor fDesc = desc.getFunction(name);
			if (fDesc == null) {
//				log().w("function not found", name);
				throw new MRuntimeException("function not found", name);
			}
			
			if (fDesc.isOneWay()) {
				MProperties prop = new MProperties("function",name);
				try {
					sendObject(prop, args);
				} catch (Exception e) {
					log().w("internal error",desc.getInterface().getCanonicalName(),method.getName(),e);
				}
			} else {
				MProperties prop = new MProperties("function",name);
				try {
					RequestResult<Object> res = sendObject(prop, args);
					// check success and throw exceptions
					if (res == null)
						throw new MRuntimeException("internal error: result is null",desc.getInterface().getCanonicalName(),method.getName());
					
					IProperties p = res.getProperties();
					String exceptionType = p.getString("exception");
					if (exceptionType != null) {
						Class<?> exceptionClass = getHelper().getClassLoader().loadClass(exceptionType);
						Throwable exception = null;
						try {
							Constructor<?> constructor = exceptionClass.getConstructor(String.class);
							exception = (Throwable) constructor.newInstance(p.getString("exceptionMessage") + " [" + p.getString("exceptionClass") + "." + p.getString("exceptionMethod") + "]" );
						} catch (Throwable t) {
							exception = (Throwable) exceptionClass.newInstance();
						}
						throw exception;
					}
					return res.getResult();
				} catch (Exception e) {
					log().w("internal error",desc.getInterface().getCanonicalName(),method.getName(),e);
				}

			}
			
			return null;
		}
		
	}
	
}
