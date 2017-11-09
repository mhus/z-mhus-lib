package de.mhus.lib.karaf.jms;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.errors.NotFoundRuntimeException;
import de.mhus.lib.jms.ClientService;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.ServerService;

public class JmsUtil {

	public static JmsManagerService getService() {
		return MApi.lookup(JmsManagerService.class);
	}
	
	/**
	 * Return the connection or null.
	 * @param name Name of the requested connection
	 * @return
	 */
	public static JmsConnection getConnection(String name) {
		JmsManagerService service = getService();
		if (service == null) return null;
		return service.getConnection(name);
	}
	
	public static JmsDataChannel getChannel(String name) {
		JmsManagerService service = getService();
		if (service == null) return null;
		return service.getChannel(name);
	}
	
	public static <I> I getObjectForInterface(Class<? extends I> ifc) {
//		synchronized (channels) {
			
			{
				JmsDataChannel channel = getChannel(ifc.getCanonicalName());
				if (channel != null)
					try {
						I o = ((ServerService<I>)channel.getChannel()).getObject();
						if (o != null) return o;
					} catch (Throwable t) {}
			}
			
			JmsManagerService service = getService();
			for (JmsDataChannel channel : service.getChannels()) {
				try {
					I o = ((ServerService<I>)channel.getChannel()).getObject();
					if (o != null) return o;
				} catch (Throwable t) {}
			}
			throw new NotFoundRuntimeException("object for interface not found", ifc);
//		}
	}
}
