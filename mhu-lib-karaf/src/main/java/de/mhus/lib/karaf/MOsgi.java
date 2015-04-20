package de.mhus.lib.karaf;

import java.util.Timer;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import de.mhus.lib.errors.NotFoundException;

public class MOsgi {
	
	public static <T> T getService(Class<T> ifc) {
		BundleContext context = FrameworkUtil.getBundle(ifc).getBundleContext();
		if (context == null) throw new NotFoundException("service context not found", ifc);
		ServiceReference<T> ref = context.getServiceReference(ifc);
		if (ref == null) throw new NotFoundException("service reference not found", ifc);
		T obj = context.getService(ref);
		if (obj == null) throw new NotFoundException("service not found", ifc);
		return obj;
	}

	public static Timer getTimer() {
		Timer timer = getService(Timer.class);
		return timer;
	}
	
}
