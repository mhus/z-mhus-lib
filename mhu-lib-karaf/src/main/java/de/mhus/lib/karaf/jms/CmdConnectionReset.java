package de.mhus.lib.karaf.jms;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;

import de.mhus.lib.jms.JmsConnection;

@Command(scope = "jms", name = "connection-reset", description = "Reset connection")
public class CmdConnectionReset implements Action {

	@Argument(index=0, name="name", required=true, description="ID of the connection or * for all", multiValued=false)
    String name;

	@Override
	public Object execute(CommandSession session) throws Exception {

		JmsManagerService service = JmsUtil.getService();
		if (service == null) {
			System.out.println("Service not found");
			return null;
		}
		
		if (name.equals("*")) {
			for (String conName : service.listConnections()) {
				System.out.println(conName);
				JmsConnection con = service.getConnection(conName);
				con.reset();
			}
		} else {
			JmsConnection con = service.getConnection(name);
			if (con == null) {
				System.out.println("Connection not found");
				return null;
			}
			
			con.reset();
		}
		System.out.println("OK");

		return null;
	}

}
