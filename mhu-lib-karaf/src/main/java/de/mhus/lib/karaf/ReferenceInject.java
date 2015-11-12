package de.mhus.lib.karaf;

import org.osgi.framework.ServiceReference;

public interface ReferenceInject {
	void setReference(ServiceReference<?> reference);
}
