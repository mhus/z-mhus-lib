package de.mhus.lib.karaf.adb;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;

@Command(scope = "adb", name = "connect", description = "Connect ADB DataSource")
public class CmdConnect implements Action {

	@Argument(index=0, name="service", required=true, description="Service Class", multiValued=false)
    String serviceName;
	
	@Option(name="-u", aliases="--update", description="Causes the driver to reconnect to the datasource",required=false)
	boolean update = false;
	
	@Option(name="-c", aliases="--cleanup", description="Cleanup unised table field and indexes - this can delete additional data",required=false)
	boolean cleanup = false;

	@Override
	public Object execute(CommandSession session) throws Exception {

		DbManagerService service = AdbUtil.getService(serviceName);
		if (service != null) {
			if (update || cleanup)
				service.updateManager(cleanup);
			else
				service.getManager(); // this call will touch the service and connect to the database
			System.out.println("OK");
		} else {
			System.out.println("Not found");
		}
		return null;
	}

}
