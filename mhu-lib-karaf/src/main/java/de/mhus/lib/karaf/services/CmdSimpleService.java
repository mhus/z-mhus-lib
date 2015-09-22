package de.mhus.lib.karaf.services;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.karaf.MOsgi;

@Command(scope = "mhus", name = "simpleservice", description = "Simple Service Interaction")
public class CmdSimpleService extends MLog implements Action {

	@Argument(index=0, name="cmd", required=true, description="list,cmd", multiValued=false)
    String cmd;

	@Argument(index=1, name="service", required=false, description="Service Name", multiValued=false)
    String serviceName;
	
	@Argument(index=2, name="service cmd", required=false, description="Cmd to the service", multiValued=false)
    String serviceCmd;

	@Argument(index=3, name="paramteters", required=false, description="Parameters", multiValued=true)
    Object[] parameters;

	@Override
	public Object execute(CommandSession session) throws Exception {
		
		if (cmd.equals("list")) {
			ConsoleTable table = new ConsoleTable();
			table.setHeaderValues("Info","Status");
			BundleContext context = FrameworkUtil.getBundle(CmdSimpleService.class).getBundleContext();
			for (ServiceReference<SimpleServiceIfc> ref : context.getServiceReferences(SimpleServiceIfc.class, null)) {
				SimpleServiceIfc service = context.getService(ref);
				table.addRowValues(service.getSimpleServiceInfo(),service.getSimpleServiceStatus());
			}
			table.print(System.out);
		} else
		if (cmd.equals("cmd")) {
			BundleContext context = FrameworkUtil.getBundle(CmdSimpleService.class).getBundleContext();
			for (ServiceReference<SimpleServiceIfc> ref : context.getServiceReferences(SimpleServiceIfc.class, null)) {
				SimpleServiceIfc service = context.getService(ref);
				if (serviceName.equals(service.getSimpleServiceInfo())) {
					System.out.println("CMD " + serviceCmd);
					service.doSimpleServiceCommand(serviceCmd, parameters);
				}
			}
		}

		return null;
	}
	
}
