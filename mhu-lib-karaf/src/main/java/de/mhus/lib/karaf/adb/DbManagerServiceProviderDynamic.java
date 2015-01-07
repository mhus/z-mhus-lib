package de.mhus.lib.karaf.adb;

import java.util.LinkedList;

import aQute.bnd.annotation.component.Component;
import de.mhus.lib.core.MLog;

@Component(provide=DbManagerServiceProvider.class,name="DbManagerServiceProviderDynamic",immediate=true)
public class DbManagerServiceProviderDynamic extends MLog implements DbManagerServiceProvider {

	private LinkedList<DbManagerService> services = new LinkedList<>();
	@Override
	public DbManagerService[] getServices() {
		synchronized(this) {
			return services.toArray(new DbManagerService[services.size()]);
		}
	}

	public void add(DbManagerService service) {
		synchronized(this) {
			services.add(service);
		}
	}
	
	public void remove(DbManagerService service) {
		synchronized(this) {
			services.remove(service);
		}
	}

	public DbManagerService remove(String name) {
		synchronized(this) {
			for (DbManagerService service : services) {
				if (name.equals(service.getServiceName())) {
					services.remove(service);
					return service;
				}
			}
		}
		return null;
	}

	
}
