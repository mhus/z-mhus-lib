package de.mhus.lib.karaf.adb;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.core.console.ConsoleTable;

@Command(scope = "adb", name = "list", description = "List all ADB Services")
@Service
public class CmdList implements Action {

	@Override
	public Object execute() throws Exception {
		
		DbManagerAdmin admin = AdbUtil.getAdmin();
		if (admin == null) {
			System.out.println("Admin not found");
			return null;
		}
		ConsoleTable table = new ConsoleTable();
		table.setHeaderValues("Nr","Service","Schema","DataSource","Managed Types");
		// iterate all services
		
		int cnt = 0;

		for ( DbManagerService service : AdbUtil.getServices(false)) {
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
