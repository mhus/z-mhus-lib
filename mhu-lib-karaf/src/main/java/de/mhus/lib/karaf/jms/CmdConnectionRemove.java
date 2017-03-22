package de.mhus.lib.karaf.jms;

import java.io.File;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.jms.JmsConnection;

@Command(scope = "jms", name = "connection-remove", description = "Remove connection")
@Service
public class CmdConnectionRemove implements Action {

	@Argument(index=0, name="name", required=true, description="ID of the connection", multiValued=false)
    String name;
	
//    @Option(name = "-o", aliases = { "--online" }, description = "Create the datasource online and not a blueprint", required = false, multiValued = false)
//    boolean online;
	
	@Override
	public Object execute() throws Exception {
	
//		if (online) {
//			JmsManagerService service = JmsUtil.getService();
//			if (service == null) {
//				System.out.println("Service not found");
//				return null;
//			}
//			
//			JmsConnection con = service.getConnection(name);
//			if (con == null) {
//				System.out.println("Connection not found");
//				return null;
//			}
//			
//			service.removeConnection(name);
//			System.out.println("OK");
//		} else {
	        File karafBase = new File(System.getProperty("karaf.base"));
	        File deployFolder = new File(karafBase, "deploy");
	        File outFile = new File(deployFolder, "jms-openwire_" + name + ".xml");
	        if (outFile.exists()) {
	        	outFile.delete();
	        } else {
	        	System.out.println("File not found " + outFile.getAbsolutePath());
	        }
//		}
		return null;
	}

}
