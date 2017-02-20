package de.mhus.lib.karaf;

import java.util.HashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

/**
 * Stores a local value for an bundle. If the bundle is stopped, the value will
 * be removed.
 * 
 * @author mikehummel
 *
 * @param <T>
 */
public class BundleLocal<T> {

	private BundleTracker<Bundle> tracker = new BundleTracker<>(FrameworkUtil.getBundle(BundleLocal.class).getBundleContext(), Bundle.STARTING | Bundle.STOPPING, new MyCustomizer());
	private HashMap<String, T> map = new HashMap<>();

	public BundleLocal<T> open() {
		tracker.open();
		return this;
	}
	
	public void close() {
		if (tracker == null) return;
		tracker.close();
		tracker = null;
	}
	
	@Override
	protected void finalize() {
		close();
	}
	
	public void put(Bundle bundle, T value) {
		put(bundle.getSymbolicName(), value);
	}
	
	
	public void put(String symbolicName, T value) {
		map.put(symbolicName, value);
	}
	
	public T get(Bundle bundle) {
		return get(bundle.getSymbolicName());
	}


	public T get(String symbolicName) {
		return map.get(symbolicName);
	}


	private class MyCustomizer implements BundleTrackerCustomizer<Bundle> {

		@Override
		public Bundle addingBundle(Bundle bundle, BundleEvent event) {
			return bundle;
		}

		@Override
		public void modifiedBundle(Bundle bundle, BundleEvent event, Bundle object) {
			
		}

		@Override
		public void removedBundle(Bundle bundle, BundleEvent event, Bundle object) {
			map.remove(bundle.getSymbolicName());
		}
		
	}
}
