package de.mhus.lib.karaf.adb;

import java.util.Map;
import java.util.TreeSet;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;

import de.mhus.lib.core.console.ConsoleTable;

@Command(scope = "adb", name = "mapping", description = "Print the mapping table of a ADB DataSource")
public class CmdMapping implements Action {

	@Argument(index=0, name="service", required=true, description="Service Class", multiValued=false)
    String serviceName;
		
	@Override
	public Object execute(CommandSession session) throws Exception {
		
		DbManagerService service = AdbUtil.getService(serviceName);
		
		ConsoleTable table = new ConsoleTable();
		table.setHeaderValues("Key","Mapping");
		
		Map<String, Object> map = service.getManager().getNameMapping();
		for (String entry : new TreeSet<String>(map.keySet())) {
			table.addRowValues(entry, String.valueOf(map.get(entry)) );
		}
		
		table.print(System.out);
		return null;
	}
	

}
