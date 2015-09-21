package de.mhus.lib.karaf.jms;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;

@Command(scope = "jms", name = "channel-remove", description = "Remove channel")
public class CmdChannelBeat implements Action {

	@Argument(index=0, name="name", required=true, description="ID of the channel", multiValued=false)
    String name;

	@Override
	public Object execute(CommandSession session) throws Exception {

		JmsManagerService service = JmsUtil.getService();
		if (service == null) {
			System.out.println("Service not found");
			return null;
		}

		if (name.equals("*")) {
			for (String cName : service.listChannels()) {
				try {
					System.out.println(cName);
					JmsDataChannel c = service.getChannel(cName);
					if (c.getChannel() == null)
						c.reset();
					c.getChannel().doBeat();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		} else {
			if (service.getChannel(name).getChannel() == null)
				service.getChannel(name).reset();
			service.getChannel(name).getChannel().doBeat();
		}
		System.out.println("OK");
		return null;
	}

}
