package de.mhus.lib.karaf.jms;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.jms.JmsConnection;

public class JmsUtil {

	public static JmsManagerService getService() {
		return MApi.lookup(JmsManagerService.class);
//		BundleContext bc = FrameworkUtil.getBundle(JmsUtil.class).getBundleContext();
//		if (bc == null) {
//			MLogUtil.log().d("BundleContext not found");
//			return null;
//		}
//		ServiceReference<JmsManagerService> ref = bc.getServiceReference(JmsManagerService.class);
//		if (ref == null) {
//			MLogUtil.log().d("JmsManager not found");
//			return null;
//		}
//		JmsManagerService obj = bc.getService(ref);
//		return obj;
	}
	
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
		JmsManagerService service = getService();
		if (service == null) throw new NotFoundException("service not found");
		return service.getObjectForInterface(ifc);
	}
	
	public static <I> I getObjectForInterface(String channel, Class<? extends I> ifc) {
		
		JmsDataChannel c = getChannel(channel);
		if (c == null) throw new NotFoundException("channel not found",channel);
		
		return c.getObject(ifc);
	}
/*	
	public static long getDestinationStatistic(JmsDestination dest) {
		JmsConnection con = dest.getConnection();
		url = con.getUrl();
		new URL(url);
	}
*/	
}
