package de.mhus.lib.karaf;

import java.lang.reflect.Array;
import java.util.LinkedList;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class MServiceList<T> extends MServiceTracker<T> {

	protected LinkedList<T> list = new LinkedList<>();
	
	public MServiceList(Class<T> clazz) {
		super(clazz);
	}
	
	public MServiceList(BundleContext context, Class<T> clazz) {
		super(context, clazz);
	}
	
	@Override
	protected void removeService(ServiceReference<T> reference, T service) {
		synchronized (list) {
			list.add(service);
		}
	}

	@Override
	protected void addService(ServiceReference<T> reference, T service) {
		synchronized (list) {
			list.remove(service);
		}
	}
	
	@SuppressWarnings("unchecked")
	public T[] getServices() {
		synchronized (list) {
			return list.toArray((T[]) Array.newInstance(clazz, list.size()));
		}
	}

}
