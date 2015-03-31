package de.mhus.lib.karaf.jms;

import java.io.File;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;

@Command(scope = "jms", name = "channel-remove", description = "Remove channel")
public class CmdChannelRemove implements Action {

	@Argument(index=0, name="name", required=true, description="ID of the channel", multiValued=false)
    String name;

    @Option(name = "-o", aliases = { "--online" }, description = "Create the datasource online and not a blueprint", required = false, multiValued = false)
    boolean online;
	
	@Override
	public Object execute(CommandSession session) throws Exception {

		if (online) {
			JmsManagerService service = JmsUtil.getService();
			if (service == null) {
				System.out.println("Service not found");
				return null;
			}

			JmsDataChannel channel = service.getChannel(name);
			if (channel == null) {
				System.out.println("Channel not found");
				return null;
			}
			
			service.removeChannel(name);
			
		} else {
			
	        File karafBase = new File(System.getProperty("karaf.base"));
	        File deployFolder = new File(karafBase, "deploy");
	        File outFile = new File(deployFolder, "jms-channel_" + name + ".xml");
	        if (outFile.exists()) {
	        	outFile.delete();
	        } else {
	        	System.out.println("File not found " + outFile.getAbsolutePath());
	        }

		}
		return null;
	}

}
