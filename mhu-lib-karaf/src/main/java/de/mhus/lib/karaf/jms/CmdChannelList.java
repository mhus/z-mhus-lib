package de.mhus.lib.karaf.jms;

import java.util.Date;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.core.MDate;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.jms.JmsChannel;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.ServerJms;

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
		table.setHeaderValues("Name","Connection","Destination","Type","Information","Connected","Closed", "Last Activity");
		for (JmsDataChannel chd : service.getChannels()) {
//			JmsDataChannel chd = service.getChannel(name);
			JmsChannel ch = chd.getChannel();
			JmsConnection con = null;
			if (ch !=null && ch.getJmsDestination() != null)
				con  = ch.getJmsDestination().getConnection();
			String i = chd.toString();
			table.addRowValues(
					chd.getName(), 
					(con == null ? "(" : "" ) + chd.getConnectionName() + (con == null ? ")" : "" ),
					ch == null ? "" : ch.getJmsDestination() ,
					ch == null ? "" : ch.getClass().getCanonicalName(),
					i,
					ch == null ? ""  : ch.isConnected(),
					ch == null ? "" : ch.isClosed(),
					ch == null || ! (ch instanceof ServerJms) ? "" : MDate.toDateTimeSecondsString(new Date( ((ServerJms)ch).getLastActivity() ))
				);
		}
		table.print(System.out);

		return null;
	}

}
