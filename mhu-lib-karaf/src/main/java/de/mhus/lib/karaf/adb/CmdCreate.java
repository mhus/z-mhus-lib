package de.mhus.lib.karaf.adb;

import java.util.HashMap;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;
import org.osgi.framework.BundleContext;

import de.mhus.lib.adb.Persistable;
import de.mhus.lib.adb.model.Field;
import de.mhus.lib.adb.model.Table;
import de.mhus.lib.core.MString;

@Command(scope = "adb", name = "create", description = "Select data from ADB DataSource ant print the results")
public class CmdCreate implements Action {
	
	@Argument(index=0, name="service", required=true, description="Service Class", multiValued=false)
    String serviceName;

	@Argument(index=1, name="type", required=true, description="Type to select", multiValued=false)
    String typeName;
	
	@Argument(index=2, name="attributes", required=false, description="Attributes for the initial creation", multiValued=true)
    String[] attributes;
	
	@Option(name="-x", description="Output parameter",required=false)
	String outputParam = null;

	@Override
	public Object execute(CommandSession session) throws Exception {
		
		DbManagerService service = AdbUtil.getService(serviceName);
		Class<? extends Persistable> type = AdbUtil.getType(service, typeName);
		String regName = service.getManager().getRegistryName(type);
		Table tableInfo = service.getManager().getTable(regName);

		Object object = service.getManager().injectObject(type.newInstance());
		
		
		
		HashMap<String, Object> attrObj = null;
		attrObj = new HashMap<>();
		if (attributes != null) {
			for (String item : attributes) {
				String key = MString.beforeIndex(item, '=').trim();
				String value = MString.afterIndex(item, '=').trim();
				attrObj.put(key, value);
			}
		}
		
		for (Field f : tableInfo.getFields()) {
			if (attrObj.containsKey(f.getName())) {
				Object v = AdbUtil.createAttribute(f.getType(), attrObj.get(f.getName()) );
				System.out.println("--- SET " + f.getName() + "  = " + v );
				f.set(object, v);
			}
		}
		
		System.out.print("*** CREATE");
		service.getManager().createObject(regName, object);
		for (Field f : tableInfo.getPrimaryKeys()) {
			System.out.print(" ");
			System.out.print(f.get(object));
		}
		System.out.println();
		
		if (outputParam != null)
			session.put(outputParam, object);
		return null;
	}
	

}
