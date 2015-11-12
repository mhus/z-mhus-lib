package de.mhus.lib.karaf;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public abstract class MServiceTracker<T> {

	private BundleContext context;
	protected Class<T> clazz;
	private ServiceTrackerCustomizer<T,T> customizer = new ServiceTrackerCustomizer<T,T>() {

		@Override
		public T addingService(ServiceReference<T> reference) {
			T service = context.getService(reference);
			if (service != null && service instanceof ReferenceInject) ((ReferenceInject)service).setReference(reference);
			MServiceTracker.this.addService(reference, service);
			return service;
		}

		@Override
		public void modifiedService(ServiceReference<T> reference, T service) {
			if (service != null && service instanceof ReferenceInject) ((ReferenceInject)service).setReference(reference);
			MServiceTracker.this.modifyService(reference, service);
		}

		@Override
		public void removedService(ServiceReference<T> reference, T service) {
			MServiceTracker.this.removeService(reference,service);
		}
		
	};
	private ServiceTracker<T, T> tracker;
	
	public MServiceTracker(Class<T> clazz) {
		this(null, clazz);
	}
	
	protected void modifyService(ServiceReference<T> reference, T service) {
		removeService(reference,service);
		addService(reference,service);
	}

	protected abstract void removeService(ServiceReference<T> reference, T service);

	protected abstract void addService(ServiceReference<T> reference, T service);

	public MServiceTracker(BundleContext context, Class<T> clazz) {
		if (context == null) context = MOsgi.getBundleContext();
		this.context = context;
		this.clazz = clazz;
	}
	
	public MServiceTracker<T> start() {
		if (tracker != null) return this;
		tracker = new ServiceTracker<>(context, clazz, customizer);
		tracker.open();
		return this;
	}
	
	public void stop() {
		if (tracker == null) return;
		tracker.close();
		tracker = null;
	}
	
	public boolean isRunning() {
		return tracker != null;
	}
	
}
