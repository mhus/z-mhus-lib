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

	@Argument(index=0, name="cmd", required=true, description="list,configure", multiValued=false)
    String cmd;

	@Argument(index=1, name="paramteters", required=false, description="Parameters", multiValued=true)
    String[] parameters;

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
		if (cmd.equals("configure")) {
			BundleContext context = FrameworkUtil.getBundle(CmdSimpleService.class).getBundleContext();
			for (ServiceReference<SimpleServiceIfc> ref : context.getServiceReferences(SimpleServiceIfc.class, null)) {
				SimpleServiceIfc service = context.getService(ref);
				if (parameters[0].equals(service.getSimpleServiceInfo())) {
					System.out.println("SET");
					service.setSimpleServiceConfiguration(parameters[1]);
				}
			}
		}

		return null;
	}
	
}
