package de.mhus.lib.karaf.jms.heartbeat;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import de.mhus.lib.karaf.jms.JmsUtil;

@Command(scope = "jms", name = "heartbeat", description = "Send heardbeat")
public class CmdHeartbeat implements Action {

	@Argument(index=0, name="enable", required=false, description="enable / disable", multiValued=false)
    Boolean enable;
	
	@Override
	public Object execute(CommandSession session) throws Exception {

		HeartbeatAdmin service = getService();
		if (service == null) {
			System.out.println("Service not found");
			return null;
		}

		if (enable == null) {
			service.sendHeartbeat();
		} else {
			service.setEnabled(enable);
		}
		
		System.out.println("OK " + (service.isEnabled() ? "enabled" : "disabled"));
		return null;
	}

	private HeartbeatAdmin getService() {
		BundleContext bc = FrameworkUtil.getBundle(JmsUtil.class).getBundleContext();
		if (bc == null) return null;
		ServiceReference<HeartbeatAdmin> ref = bc.getServiceReference(HeartbeatAdmin.class);
		if (ref == null) return null;
		HeartbeatAdmin obj = bc.getService(ref);
		return obj;
	}

	
}
