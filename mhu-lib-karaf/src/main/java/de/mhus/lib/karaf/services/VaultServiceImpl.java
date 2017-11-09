package de.mhus.lib.karaf.services;

import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import de.mhus.lib.core.vault.DefaultVault;
import de.mhus.lib.core.vault.MVault;
import de.mhus.lib.core.vault.MVaultUtil;

@Component(provide=MVault.class,immediate=true)
public class VaultServiceImpl extends DefaultVault {
	
	@Activate
	public void doActivate(ComponentContext ctx) {
		MVaultUtil.checkDefault(this);
	}


}
