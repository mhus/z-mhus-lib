package de.mhus.lib.karaf.adb;

import java.util.LinkedList;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import de.mhus.lib.core.MLog;

@Component(provide=DbManagerServiceProvider.class,name="DbManagerServiceProviderOsgi",immediate=true)
public class DbManagerServiceProviderOsgi extends MLog implements DbManagerServiceProvider {

	private BundleContext context;

	@Activate
	public void doActivate(ComponentContext ctx) {
		context = ctx.getBundleContext();
	}

	@Deactivate
	public void doDeactivate(ComponentContext ctx) {
		context = null;
	}
	
	@Override
	public DbManagerService[] getServices() {

		LinkedList<DbManagerService> out = new LinkedList<>();
		try {
			for ( ServiceReference<DbManagerService> sr : context.getServiceReferences(DbManagerService.class, null)) {
				DbManagerService service = context.getService(sr);
				out.add(service);
			}
		} catch (InvalidSyntaxException e) {
			log().e(e); // should not happen
		}
		return out.toArray(new DbManagerService[ out.size()]);
	}

}
