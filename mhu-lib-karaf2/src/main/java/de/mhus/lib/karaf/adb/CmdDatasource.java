package de.mhus.lib.karaf.adb;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;

@Command(scope = "adb", name = "datasource", description = "Update ADB DataSource")
public class CmdDatasource implements Action {
	
	@Argument(index=0, name="service", required=true, description="Service Class", multiValued=false)
    String serviceName;

	@Argument(index=1, name="source", required=false, description="Data Source", multiValued=false)
    String sourceName;
	
	@Override
	public Object execute(CommandSession session) throws Exception {

		int cnt = 0;
		
		for ( DbManagerService service : AdbUtil.getAdmin().getServices()) {
//			if (service.isConnected()) {
				if (service.getClass().getCanonicalName().equals(serviceName)) {
					if (sourceName == null)
						service.updateManager(false);
					else
						service.setDataSourceName(sourceName);
					cnt++;
				}
//			}
		}
		System.out.println("Updated: " + cnt);
		return null;
	}

}
