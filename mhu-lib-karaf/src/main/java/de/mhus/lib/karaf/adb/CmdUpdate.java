package de.mhus.lib.karaf.adb;

import java.util.HashMap;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;

import de.mhus.lib.adb.model.Field;
import de.mhus.lib.adb.model.Table;
import de.mhus.lib.core.MString;

@Command(scope = "adb", name = "update", description = "Update a single object in database")
public class CmdUpdate implements Action {
	
	@Argument(index=0, name="service", required=true, description="Service Class", multiValued=false)
    String serviceName;

	@Argument(index=1, name="type", required=true, description="Type to select", multiValued=false)
    String typeName;
	
	@Argument(index=2, name="id", required=true, description="Id of the object or query", multiValued=false)
    String id;

	@Argument(index=3, name="attributes", required=false, description="Attributes to update, e.g user=alfons", multiValued=true)
    String[] attributes;
	
	@Option(name="-x", description="Output parameter",required=false)
	String outputParam = null;

	@Option(name="-f", description="Force Save",required=false)
	boolean force = false;
	
	@Option(name="-w", description="RAW Save",required=false)
	boolean raw = false;
	
	@Override
	public Object execute(CommandSession session) throws Exception {
		
		Object output = null;
		
		DbManagerService service = AdbUtil.getService(serviceName);
		Class<?> type = AdbUtil.getType(service, typeName);
		
		HashMap<String, Object> attrObj = null;
		attrObj = new HashMap<>();
		if (attributes != null) {
			for (String item : attributes) {
				String key = MString.beforeIndex(item, '=').trim();
				String value = MString.afterIndex(item, '=').trim();
				attrObj.put(key, value);
			}
		}
		
		String regName = service.getManager().getRegistryName(type);
		Table tableInfo = service.getManager().getTable(regName);
		
		for (Object object : AdbUtil.getObjects(service, type, id)) {
		
			System.out.println(">>> UPDATE " + object);
			
			for (Field f : tableInfo.getFields()) {
				if (attrObj.containsKey(f.getName())) {
					Object v = AdbUtil.createAttribute(f.getType(), attrObj.get(f.getName()) );
					System.out.println("--- SET " + f.getName() + "  = " + v );
					f.set(object, v );
				}
			}
			
			System.out.println("*** SAVE");
			if (force)
				service.getManager().saveObjectForce(regName, object, raw);
			else
				service.getManager().saveObject(regName, object);
			output = object;
		}	
		if (outputParam != null)
			session.put(outputParam, output);
		return null;
	}
	

}
