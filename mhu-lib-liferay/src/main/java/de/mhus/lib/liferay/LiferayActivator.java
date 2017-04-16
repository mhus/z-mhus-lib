package de.mhus.lib.liferay;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.mhus.lib.core.MLog;
import de.mhus.lib.liferay.osgi.CmdLiferayMhus;

public class LiferayActivator extends MLog implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		log().i("Start");
		context.registerService(CommandProvider.class, new CmdLiferayMhus(), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		log().i("Stop");
	}

}
