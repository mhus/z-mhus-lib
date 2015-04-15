package de.mhus.lib.karaf.adb;

import java.util.LinkedList;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.osgi.framework.BundleContext;

import de.mhus.lib.adb.model.Field;
import de.mhus.lib.adb.model.Table;
import de.mhus.lib.core.console.ConsoleTable;

@Command(scope = "adb", name = "info", description = "Show information of a type")
public class CmdInfo implements Action {
	
	@Argument(index=0, name="service", required=true, description="Service Class", multiValued=false)
    String serviceName;

	@Argument(index=1, name="type", required=true, description="Type to select", multiValued=false)
    String typeName;

	@Override
	public Object execute(CommandSession session) throws Exception {
		
		DbManagerService service = AdbUtil.getService(serviceName);
		Class<?> type = AdbUtil.getType(service, typeName);
		
		String regName = service.getManager().getRegistryName(type);
		Table tableInfo = service.getManager().getTable(regName);
		
		ConsoleTable out = new ConsoleTable();
		out.setHeaderValues("Field Name","Type","PrimaryKey","Persistent","Mapping");
		
		LinkedList<String> primaryNames = new LinkedList<>();
		for (Field f : tableInfo.getPrimaryKeys())
			primaryNames.add(f.getName());
		
		for (Field f : tableInfo.getFields())
			out.addRowValues(
					f.getName(), 
					f.getType().getSimpleName(), 
					String.valueOf(primaryNames.contains(f.getName())), 
					String.valueOf(f.isPersistent()),
					f.getMappedName() 
					);
		
		out.print(System.out);
		
		return null;
	}
	

}
