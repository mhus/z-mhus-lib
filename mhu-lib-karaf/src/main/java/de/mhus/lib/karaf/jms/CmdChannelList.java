package de.mhus.lib.karaf.jms;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.jms.JmsChannel;
import de.mhus.lib.jms.JmsConnection;

@Command(scope = "jms", name = "channel-list", description = "List Channels")
@Service
public class CmdChannelList implements Action {

	@Override
	public Object execute() throws Exception {

		JmsManagerService service = JmsUtil.getService();
		if (service == null) {
			System.out.println("Service not found");
			return null;
		}
		
		ConsoleTable table = new ConsoleTable();
		table.setHeaderValues("Name","Connection","Destination","Type","Information","Connected","Closed");
		for (String name : service.listChannels()) {
			JmsDataChannel chd = service.getChannel(name);
			JmsChannel ch = chd.getChannel();
			JmsConnection con = null;
			if (ch !=null && ch.getDestination() != null)
				con  = ch.getDestination().getConnection();
			String i = chd.getInformation();
			table.addRowValues(
					chd.getName(), 
					(con == null ? "(" : "" ) + chd.getConnectionName() + (con == null ? ")" : "" ),
					ch == null ? "" : ch.getDestination() ,
					ch == null ? "" : (ch instanceof ChannelWrapper ? ((ChannelWrapper)ch).getType() : ch.getClass().getCanonicalName()),
					i,
					ch == null ? ""  : ch.isConnected(),
					ch == null ? "" : ch.isClosed()
				);
		}
		table.print(System.out);

		return null;
	}

}
