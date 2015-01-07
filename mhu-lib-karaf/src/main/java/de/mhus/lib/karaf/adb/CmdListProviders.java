package de.mhus.lib.karaf.adb;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Command;
import org.osgi.framework.BundleContext;

import de.mhus.lib.core.console.ConsoleTable;

@Command(scope = "adb", name = "providers", description = "List all ADB Service Providers")
public class CmdListProviders implements Action {

	private BundleContext context;
	
	public void setContext(BundleContext context) {
        this.context = context;
    }

	@Override
	public Object execute(CommandSession session) throws Exception {
		ConsoleTable table = new ConsoleTable();
		table.setHeaderValues("Provider","Services");
		// iterate all services
		
		for ( DbManagerServiceProvider provider : AdbUtil.getServiceProviders(context)) {
			
			StringBuffer types = new StringBuffer();
			for (DbManagerService service : provider.getServices()) {
				if (types.length() != 0) types.append(',');
				types.append(service.getServiceName());
			}
			
			table.addRowValues(
					provider.getClass().getSimpleName(),
					types.toString()
				);
		}
		table.print(System.out);
		return null;
	}

}
