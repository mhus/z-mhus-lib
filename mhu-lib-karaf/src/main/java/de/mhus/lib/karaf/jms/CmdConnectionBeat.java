package de.mhus.lib.karaf.jms;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.jms.JmsConnection;

@Command(scope = "jms", name = "connection-beat", description = "Beat the connection, load connections and channels")
@Service
public class CmdConnectionBeat implements Action {

	@Option(name="-c",aliases="--channels",description="Beat also all channels",required=false)
	boolean channels = false;
	
	@Override
	public Object execute() throws Exception {

		JmsManagerService service = JmsUtil.getService();
		if (service == null) {
			System.out.println("Service not found");
			return null;
		}

		service.doBeat();
		if (channels)
			service.doChannelBeat();
		System.out.println("OK");

		return null;
	}

}
