package de.mhus.lib.karaf.jms;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.jms.JmsChannel;
import de.mhus.lib.jms.JmsConnection;

@Command(scope = "jms", name = "connection-info", description = "Connection Information")
@Service
public class CmdConnectionInfo implements Action {

	@Argument(index=0, name="name", required=true, description="ID of the connection", multiValued=false)
    String name;
	
	@Override
	public Object execute() throws Exception {
	
		JmsManagerService service = JmsUtil.getService();
		if (service == null) {
			System.out.println("Service not found");
			return null;
		}
		
		JmsConnection con = service.getConnection(name);
		if (con == null) {
			System.out.println("Connection not found");
			return null;
		}
		
		ConsoleTable table = new ConsoleTable();
		table.setHeaderValues("Queue","Type");
		for (JmsChannel base : con.getChannelList()) {
			table.addRowValues(base.toString(),base.getClass().getSimpleName());
		}
		table.print(System.out);
		
		return null;
	}

}
