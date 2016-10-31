package de.mhus.lib.karaf.jms;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.jms.JmsConnection;

@Command(scope = "jms", name = "connection-list", description = "Remove connection")
@Service
public class CmdConnectionList implements Action {

	@Override
	public Object execute() throws Exception {
		JmsManagerService service = JmsUtil.getService();
		if (service == null) {
			System.out.println("Service not found");
			return null;
		}
		
		ConsoleTable table = new ConsoleTable();
		table.setHeaderValues("Name","Url","User","Connected","Closed");
		for (String name : service.listConnections()) {
			try {
				JmsConnection con = service.getConnection(name);
				table.addRowValues(name,con.getUrl(),con.getUser(),con.isConnected(),con.isClosed());
			} catch (Throwable t) {}
		}
		table.print(System.out);
		
		return null;
	}

}
