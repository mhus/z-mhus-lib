package de.mhus.lib.karaf.jms;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;

import de.mhus.lib.karaf.TemplateUtils;

@Command(scope = "jms", name = "channel-add", description = "add channel")
public class CmdChannelAdd implements Action {

	@Argument(index=0, name="name", required=true, description="ID of the channel", multiValued=false)
    String name;

	@Argument(index=1, name="connection", required=true, description="ID of the connection", multiValued=false)
    String connection;

	@Argument(index=2, name="destination", required=true, description="Name of the destination queue or topic", multiValued=false)
    String destination;

	@Argument(index=3, name="interface", required=true, description="Interface or Implementation", multiValued=false)
    String ifc;

    @Option(name = "-o", aliases = { "--online" }, description = "Create the datasource online and not a blueprint", required = false, multiValued = false)
    boolean online;
	
    @Option(name = "-t", aliases = { "--topic" }, description = "Destinantion is a topic", required = false, multiValued = false)
    boolean topic;

    @Option(name = "-s", aliases = { "--service" }, description = "Channel is a service, not a client", required = false, multiValued = false)
    boolean service;
    
	@Override
	public Object execute(CommandSession session) throws Exception {

		if (online) {
			
			JmsManagerService service = JmsUtil.getService();
			if (service == null) {
				System.out.println("Service not found");
				return null;
			}

			JmsDataChannelImpl impl = new JmsDataChannelImpl();
			impl.setName(name);
			impl.setConnectionName(connection);
			impl.setDestination(destination);
			impl.setDestinationTopic(topic);
			if (this.service)
				impl.setImplementation(ifc);
			else
				impl.setIfc(ifc);
			
			service.addChannel(impl);
			
		} else {
			
	        File karafBase = new File(System.getProperty("karaf.base"));
	        File deployFolder = new File(karafBase, "deploy");
	        File outFile = new File(deployFolder, "jms-channel_" + name + ".xml");

	        HashMap<String, String> properties = new HashMap<String, String>();
	        properties.put("connection", connection);
	        properties.put("destination", destination);
	        properties.put("topic", String.valueOf(topic));
	        properties.put("interface", service ? "" : ifc);
	        properties.put("implementation", service ? ifc : "");
	        properties.put("name", name);
	        String templateFile = "jms-channel.xml";
            InputStream is = this.getClass().getResourceAsStream(templateFile);
            if (is == null) {
                throw new IllegalArgumentException("Template resource " + templateFile + " doesn't exist");
            }
            TemplateUtils.createFromTemplate(outFile, is, properties);

			
		}
		
		return null;
	}

}
