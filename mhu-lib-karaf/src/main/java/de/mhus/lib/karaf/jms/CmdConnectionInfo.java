package de.mhus.lib.karaf.jms;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;

import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.jms.JmsBase;
import de.mhus.lib.jms.JmsConnection;

@Command(scope = "jms", name = "connection-info", description = "Connection Information")
public class CmdConnectionInfo implements Action {

	@Argument(index=0, name="name", required=true, description="ID of the connection", multiValued=false)
    String name;
	
	@Override
	public Object execute(CommandSession session) throws Exception {
	
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
		for (JmsBase base : con.getListBases()) {
			table.addRowValues(base.toString(),base.getClass().getSimpleName());
		}
		table.print(System.out);
		
		return null;
	}

}
