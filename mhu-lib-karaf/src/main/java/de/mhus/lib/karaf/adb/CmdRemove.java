package de.mhus.lib.karaf.adb;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;
import org.osgi.framework.BundleContext;

@Command(scope = "adb", name = "remove", description = "Remove a single object from database")
public class CmdRemove implements Action {

	private BundleContext context;
	
	@Argument(index=0, name="service", required=true, description="Service Class", multiValued=false)
    String serviceName;

	@Argument(index=1, name="type", required=true, description="Type to select", multiValued=false)
    String typeName;
	
	@Argument(index=2, name="id", required=true, description="Id of the object or query", multiValued=false)
    String id;
	
	@Option(name="-x", description="Output parameter",required=false)
	String outputParam = null;

	public void setContext(BundleContext context) {
        this.context = context;
    }

	@Override
	public Object execute(CommandSession session) throws Exception {
		
		Object output = null;
		
		DbManagerService service = AdbUtil.getService(context,serviceName);
		Class<?> type = AdbUtil.getType(service, typeName);
				
		String regName = service.getManager().getRegistryName(type);
		
		for (Object object : AdbUtil.getObjects(service, type, id)) {
		
			System.out.println("*** REMOVE " + object);
			service.getManager().removeObject(regName, object);
			output = object;
		}
		if (outputParam != null)
			session.put(outputParam, output);
		return null;
	}
	

}
