package de.mhus.lib.karaf.adb;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

@Command(scope = "adb", name = "connect", description = "Connect ADB DataSource")
@Service
public class CmdConnect implements Action {

	@Argument(index=0, name="service", required=true, description="Service Class", multiValued=false)
    String serviceName;
	
	@Option(name="-u", aliases="--update", description="Causes the driver to reconnect to the datasource",required=false)
	boolean update = false;
	
	@Option(name="-c", aliases="--cleanup", description="Cleanup unised table field and indexes - this can delete additional data",required=false)
	boolean cleanup = false;

	@Override
	public Object execute() throws Exception {

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
