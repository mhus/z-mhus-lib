package de.mhus.lib.karaf.services;

import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.mhus.lib.core.vault.MVault;
import de.mhus.lib.core.vault.VaultSource;
import de.mhus.lib.karaf.MServiceTracker;

@Component(provide=SimpleServiceIfc.class,immediate=true)
public class VaultManagerImpl extends SimpleService {
	
	MServiceTracker<VaultSource> services;
	private MVault vault;
	
	@Activate
	public void doActivate(ComponentContext ctx) {
		services = new MServiceTracker<VaultSource>(ctx.getBundleContext(),VaultSource.class) {
			
			@Override
			protected void removeService(ServiceReference<VaultSource> reference, VaultSource service) {
//				MVault vault = MVaultUtil.loadDefault();
				vault.unregisterSource(service.getName());
			}
			
			@Override
			protected void addService(ServiceReference<VaultSource> reference, VaultSource service) {
//				MVault vault = MVaultUtil.loadDefault();
				vault.registerSource(service);
			}
		}.start();
	}
	
	@Deactivate
	public void doDeactivate(ComponentContext ctx) {
		if (services != null)
			services.stop();
		services = null;
	}

	@Reference(service=MVault.class)
	public void setVault(MVault vault) {
		log().i("Reference Vault");
		this.vault = vault;
	}
	

}
