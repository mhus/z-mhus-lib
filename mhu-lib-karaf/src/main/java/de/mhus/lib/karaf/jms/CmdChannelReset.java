package de.mhus.lib.karaf.jms;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

@Command(scope = "jms", name = "channel-reset", description = "Reset channels")
@Service
public class CmdChannelReset implements Action {

	@Argument(index=0, name="name", required=true, description="ID of the channel or * for all", multiValued=false)
    String name;

	@Override
	public Object execute() throws Exception {

		JmsManagerService service = JmsUtil.getService();
		if (service == null) {
			System.out.println("Service not found");
			return null;
		}

		if (name == null || name.equals("*"))
			for (JmsDataChannel c : service.getChannels()) {
				try {
					System.out.println(c);
					c.reset();
					if (c.getChannel() != null) {
						c.getChannel().reset();
						c.getChannel().open();
					}else
						System.out.println("... channel is null");
				} catch (Throwable t) {
					System.out.println(t);
				}
			}
		else {
			JmsDataChannel channel = service.getChannel(name);
			if (channel == null) {
				System.out.println("Channel not found");
				return null;
			}
			channel.getChannel().reset();
			channel.getChannel().open();
		}
		System.out.println("OK");
		return null;
	}

}
