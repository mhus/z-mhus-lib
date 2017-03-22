package de.mhus.lib.karaf.jms;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

@Command(scope = "jms", name = "channel-remove", description = "Remove channel")
@Service
public class CmdChannelBeat implements Action {

	@Argument(index=0, name="name", required=false, description="ID of the channel", multiValued=false)
    String name;

	@Override
	public Object execute() throws Exception {

		JmsManagerService service = JmsUtil.getService();
		if (service == null) {
			System.out.println("Service not found");
			return null;
		}

		if (name == null || name.equals("*")) {
			service.doChannelBeat();
//			for (JmsDataChannel c : service.getChannels()) {
//				try {
//					System.out.println(c);
//					if (c.getChannel() == null)
//						c.reset();
//					c.getChannel().doBeat();
//				} catch (Throwable t) {
//					t.printStackTrace();
//				}
//			}
		} else {
			if (service.getChannel(name).getChannel() == null)
				service.getChannel(name).reset();
			service.getChannel(name).getChannel().doBeat();
		}
		System.out.println("OK");
		return null;
	}

}
