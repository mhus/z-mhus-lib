package de.mhus.lib.karaf.jms;

import java.util.List;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;

import de.mhus.lib.core.MThread;
import de.mhus.lib.jms.ping.Ping;

@Command(scope = "jms", name = "ping", description = "ping")
public class CmdPing implements Action {

	@Argument(index=0, name="cmd", required=false, description="command", multiValued=false)
    String cmd;

    @Option(name = "-t", aliases = { "--target" }, description = "Channel to use", required = false, multiValued = false)
    String channel;
	    
    @Option(name = "-c", aliases = { "--count" }, description = "Count", required = false, multiValued = false)
    int count = 1;
    
    @Option(name = "-s", aliases = { "--size" }, description = "Package Size", required = false, multiValued = false)
    int size = 1024;

    @Option(name = "-d", aliases = { "--sleep" }, description = "Sleep", required = false, multiValued = false)
    int sleep = 1000;
    
	@Override
	public Object execute(CommandSession session) throws Exception {

		Ping ping = null;
		if (channel == null) {
			ping = JmsUtil.getObjectForInterface(Ping.class);
		} else {
			ping = JmsUtil.getObjectForInterface(channel, Ping.class);
		}
		
		if (cmd == null) cmd = "ping";

		for (int i = 0; i < count; i++) {
			switch (cmd) {
			case "ping": {
				byte[] buf = new byte[size];
				long start = System.currentTimeMillis();
				List<String> res = ping.ping(buf);
				long diff = System.currentTimeMillis() - start;
				System.out.println("Ping " + size + " bytes in " + diff + " ms " + res);
			} break;
			case "timediff": {
				long diff = ping.timeDiff(System.currentTimeMillis());
				System.out.println("Time difference " + diff);
			} break;
			case "hostname": {
				String host = ping.hostname();
				System.out.println("Hostname " + host);
			} break;
			}
			
			if (i+1 != count)
				MThread.sleep(sleep);
		}
		return null;
	}

}
