package de.mhus.lib.karaf.vaadin;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbstractComponent;

import de.mhus.lib.vaadin.aqua.AquaLifecycle;
import de.mhus.lib.vaadin.aqua.AquaSpace;
import de.mhus.lib.vaadin.aqua.AquaUi;

public class AquaKarafUi extends AquaUi {

	private ServiceTracker<AquaSpace,AquaSpace> spaceTracker;
	private BundleContext context;

	@Override
	protected void init(VaadinRequest request) {
		super.init(request);
		
        context = FrameworkUtil.getBundle(getClass()).getBundleContext();
		spaceTracker = new ServiceTracker<>(context, AquaSpace.class, new AquaSpaceServiceTrackerCustomizer() );
		spaceTracker.open();

	}
	
	@Override
	public void close() {
		synchronized (this) {
			spaceTracker.close();
			spaceList.clear();
			for (AbstractComponent v : spaceInstanceList.values())
				if (v instanceof AquaLifecycle) ((AquaLifecycle)v).doDestroy();
			spaceInstanceList.clear();
		}
		super.close();
	}

	private class AquaSpaceServiceTrackerCustomizer implements ServiceTrackerCustomizer<AquaSpace,AquaSpace> {

		@Override
		public AquaSpace addingService(
				ServiceReference<AquaSpace> reference) {
			synchronized (this) {
				AquaSpace service = context.getService(reference);
				spaceList.put(service.getName(),service);
				if (desktop != null) desktop.refreshSpaceList(spaceList);
				return service;
			}
		}

		@Override
		public void modifiedService(
				ServiceReference<AquaSpace> reference,
				AquaSpace service) {
			synchronized (this) {
				spaceList.remove(service.getName());
				AbstractComponent v = spaceInstanceList.remove(service.getName());
				if (v instanceof AquaLifecycle) ((AquaLifecycle)v).doDestroy();
				service = context.getService(reference);
				spaceList.put(service.getName(),service);
				if (desktop != null) desktop.refreshSpaceList(spaceList);
			}
		}

		@Override
		public void removedService(ServiceReference<AquaSpace> reference,
				AquaSpace service) {
			synchronized (this) {
				spaceList.remove(service.getName());
				AbstractComponent v = spaceInstanceList.remove(service.getName());
				if (v instanceof AquaLifecycle) ((AquaLifecycle)v).doDestroy();
				if (desktop != null) desktop.refreshSpaceList(spaceList);
			}
		}
	}

}
