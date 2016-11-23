package de.mhus.lib.karaf;

import java.lang.reflect.Array;
import java.util.HashMap;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class MServiceMap<T> extends MServiceTracker<T> {

	protected HashMap<String,T> map = new HashMap<>();
	
	public MServiceMap(Class<T> clazz) {
		super(clazz);
	}
	
	public MServiceMap(BundleContext context, Class<T> clazz) {
		super(context, clazz);
	}
	
	@Override
	protected void removeService(ServiceReference<T> reference, T service) {
		synchronized (map) {
			map.remove(getServiceName(reference, service));
		}
	}

	protected String getServiceName(ServiceReference<T> reference, T service) {
		return MOsgi.getServiceName(reference);
	}

	@Override
	protected void addService(ServiceReference<T> reference, T service) {
		synchronized (map) {
			map.put(getServiceName(reference, service), service);
		}
	}
	
	@SuppressWarnings("unchecked")
	public T[] getServices() {
		synchronized (map) {
			return map.values().toArray((T[]) Array.newInstance(clazz, map.size()));
		}
	}
	
	public String[] getNames() {
		synchronized (map) {
			return map.keySet().toArray(new String[map.size()]);
		}
	}

	public T getService(String name) {
		synchronized (map) {
			return map.get(name);
		}
	}
	
}
