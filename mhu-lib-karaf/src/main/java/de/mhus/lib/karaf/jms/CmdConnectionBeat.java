package de.mhus.lib.karaf.jms;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;

import de.mhus.lib.jms.JmsConnection;

@Command(scope = "jms", name = "connection-beat", description = "Remove connection")
public class CmdConnectionBeat implements Action {

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
		
		con.doBaseBeat();
		System.out.println("OK");

		return null;
	}

}
