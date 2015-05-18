package de.mhus.lib.karaf;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import aQute.bnd.annotation.component.Component;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.system.ISingleton;
import de.mhus.lib.mutable.KarafSingletonImpl;

@Component(name="de.mhus.lib.karaf.MhuConfigUpdater",provide=ManagedService.class,immediate=true)
public class MhuConfigUpdater implements ManagedService {

	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {

		if (MSingleton.isDirtyTrace())
			System.out.println("--- Update OSGI Configuration" + config);
		
		if (config == null)
			return;
		
		ISingleton singleton = MSingleton.get();
		if (singleton instanceof KarafSingletonImpl)
			((KarafSingletonImpl)singleton).updateOsgiConfig(config);
		else
			System.out.println("--- MSingleton is not a karaf singleton");
	}

}
