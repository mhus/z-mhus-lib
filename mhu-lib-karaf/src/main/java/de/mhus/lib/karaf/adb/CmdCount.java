package de.mhus.lib.karaf.adb;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.console.Session;

import de.mhus.lib.adb.model.Field;
import de.mhus.lib.adb.model.Table;
import de.mhus.lib.core.MString;

@Command(scope = "adb", name = "count", description = "Select data from ADB DataSource ant print the count of found objects")
@Service
public class CmdCount implements Action {
	
	@Argument(index=0, name="service", required=true, description="Service Class", multiValued=false)
    String serviceName;

	@Argument(index=1, name="type", required=true, description="Type to select", multiValued=false)
    String typeName;
	
	@Argument(index=2, name="qualification", required=false, description="Select qualification", multiValued=false)
    String qualification;

	@Argument(index=3, name="attributes", required=false, description="Attributes for the select, e.g user=alfons", multiValued=true)
    String[] attributes;

	@Option(name="-f", aliases="--full", description="Print the full value content also if it's very long",required=false)
	boolean full = false;

	@Option(name="-m", aliases="--max", description="Maximum amount of chars for a value (if not full)",required=false)
	int max = 40;

	@Option(name="-x", description="Output parameter",required=false)
	String outputParam = null;
	
    @Reference
    private Session session;

	@Override
	public Object execute() throws Exception {
				
		DbManagerService service = AdbUtil.getService(serviceName);
		Class<?> type = AdbUtil.getType(service, typeName);
		
		HashMap<String, Object> attrObj = null;
		if (attributes != null) {
			attrObj = new HashMap<>();
			for (String item : attributes) {
				String key = MString.beforeIndex(item, '=').trim();
				String value = MString.afterIndex(item, '=').trim();
				attrObj.put(key, value);
			}
		}
		
		
		String regName = service.getManager().getRegistryName(type);
		Table tableInfo = service.getManager().getTable(regName);

		List<Field> pkeys = tableInfo.getPrimaryKeys();
		final HashSet<String> pkNames = new HashSet<>();
		for (Field f : pkeys)
			pkNames.add(f.getName());
				
		long count = service.getManager().getCountByQualification(type, qualification, attrObj);
		
		System.out.println(count);
		
		if (outputParam != null)
			session.put(outputParam, count);
		return null;
	}
	

}
