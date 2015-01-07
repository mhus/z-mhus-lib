package de.mhus.lib.karaf.adb;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Command;
import org.osgi.framework.BundleContext;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.core.console.ConsoleTable;

@Command(scope = "adb", name = "list", description = "List all ADB Services")
public class CmdList implements Action {

	private BundleContext context;
	
	public void setContext(BundleContext context) {
        this.context = context;
    }

	@Override
	public Object execute(CommandSession session) throws Exception {
		ConsoleTable table = new ConsoleTable();
		table.setHeaderValues("Nr","Service","Schema","DataSource","Managed Types");
		// iterate all services
		
		int cnt = 0;

		for ( DbManagerService service : AdbUtil.getServices(context, false)) {
			if (service.isConnected()) {
				DbManager manager = service.getManager();
				StringBuffer types = new StringBuffer();
				for (Class<?> type : manager.getSchema().getObjectTypes()) {
					if (types.length() != 0) types.append(',');
					types.append(type.getSimpleName());
				}
				table.addRowValues(
						"*" + cnt,
						service.getServiceName(),
						manager.getSchema().getClass().getSimpleName(),
						service.getDataSourceName(),
						types.toString()
					);
			} else {
				table.addRowValues(
						"*" + cnt,
						service.getServiceName(),
						"[not connected]", 
						service.getDataSourceName(),
						""
					);
			}
			cnt++;
		}
		table.print(System.out);
		return null;
	}

}
