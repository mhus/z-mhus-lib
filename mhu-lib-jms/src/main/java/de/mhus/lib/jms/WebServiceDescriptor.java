package de.mhus.lib.jms;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.pojo.FunctionsOnlyStrategy;
import de.mhus.lib.core.pojo.PojoAction;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoParser;

public class WebServiceDescriptor extends ServiceDescriptor {

	private Object service;
	private PojoModel model;

	public WebServiceDescriptor(Object service) {
		super(findIfc(service));
		this.service = service;
		model = new PojoParser().parse(service, new FunctionsOnlyStrategy(true, WebMethod.class) ).getModel();
		
		for (String name : model.getActionNames()) {
			PojoAction act = model.getAction(name);
			functions.put(name, new WebServiceFunction(act));
		}
		
	}
	
	@Override
	public Object getObject() {
		return service;
	}

	private static Class<?> findIfc(Object service) {
		// TODO traverse thru all ifcs
		Class<?> c = service instanceof Class ? (Class<?>)service : service.getClass();
		if (c.isAnnotationPresent(WebService.class)) return c;
		for (Class<?> i : c.getInterfaces()) {
			if (i.isAnnotationPresent(WebService.class)) return i;
		}
		return c;
	}

	private class WebServiceFunction extends FunctionDescriptor {

		private PojoAction act;

		public WebServiceFunction(PojoAction act) {
			this.act = act;
//			oneWay = act.getAnnotation(Oneway.class) != null || act.getReturnType() == null;
			oneWay = act.getAnnotation(Oneway.class) != null;
			returnType = act.getReturnType();
			if (returnType == null) returnType = Void.class;
		}

		@Override
		public RequestResult<Object> doExecute(AbstractProperties properties,
				Object[] obj) {
			
			// TODO check special case for direct handling
			
			MProperties p = new MProperties();
			Object res = null;
			Throwable t = null;
			try {
				res = act.doExecute(service, obj);
			} catch (IOException e) {
				t = e.getCause();
				if (t instanceof InvocationTargetException) {
					t = t.getCause();
				}
				if (t == null) t = e;
			} catch (Throwable e) {
				t = e;
			}
			if (t != null) {
				//TODO move into ServerService and ServerJsonService to define the protocol in ONE place
				p.setString(ClientService.PROP_EXCEPION_TYPE, t.getClass().getCanonicalName());
				p.setString(ClientService.PROP_EXCEPION_TEXT, t.getMessage());
				p.setString(ClientService.PROP_EXCEPION_CLASS, act.getManagedClass().getCanonicalName());
				p.setString(ClientService.PROP_EXCEPTION_METHOD, act.getName());
				
				log().t(act.getManagedClass().getCanonicalName(),act.getName(),t);
			}
			return new RequestResult<Object>(res, p);
		}
		
	}
	
}
