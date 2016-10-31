package de.mhus.lib.karaf.jms;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.jms.JmsConnection;

@Command(scope = "jms", name = "connection-beat", description = "Beat all channels")
@Service
public class CmdConnectionBeat implements Action {

	@Argument(index=0, name="name", required=true, description="ID of the connection or * for all", multiValued=false)
    String name;

	@Override
	public Object execute() throws Exception {

		JmsManagerService service = JmsUtil.getService();
		if (service == null) {
			System.out.println("Service not found");
			return null;
		}
		
		if (name.equals("*")) {
			for (String conName : service.listConnections()) {
				System.out.println(conName);
				JmsConnection con = service.getConnection(conName);
				con.doChannelBeat();
			}
		} else {
			JmsConnection con = service.getConnection(name);
			if (con == null) {
				System.out.println("Connection not found");
				return null;
			}
			
			con.doChannelBeat();
		}
		System.out.println("OK");

		return null;
	}

}
