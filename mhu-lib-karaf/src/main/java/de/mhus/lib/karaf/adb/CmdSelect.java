package de.mhus.lib.karaf.adb;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;
import org.osgi.framework.BundleContext;

import de.mhus.lib.adb.DbCollection;
import de.mhus.lib.adb.model.Field;
import de.mhus.lib.adb.model.Table;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.console.ConsoleTable;

@Command(scope = "adb", name = "select", description = "Select data from ADB DataSource ant print the results")
public class CmdSelect implements Action {

	private BundleContext context;
	
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

	@Option(name="-o", aliases="--out", description="Comma separated list of fields to print",required=false)
	String fieldsComma = null;
	
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
		
		String[] fields = null;
		if (fieldsComma != null) fields = fieldsComma.split(",");
		
		LinkedList<Field> fieldList = new LinkedList<>();
		for (Field f : tableInfo.getFields())
			if (fields == null)
				fieldList.add(f);
			else {
				String fn = f.getName();
				for (String fn2 : fields) {
					if (fn2.equals(fn)) {
						fieldList.add(f);
						break;
					}
				}
			}
		
		Collections.sort(fieldList,new Comparator<Field>() {

			@Override
			public int compare(Field o1, Field o2) {
				boolean pk1 = pkNames.contains(o1.getName());
				boolean pk2 = pkNames.contains(o2.getName());
				if (pk1 == pk2)
					return o1.getName().compareTo(o2.getName());
				if (pk1) return -1;
				//if (pk2) return 1;
				return 1;
			}
		});
		
		
		ConsoleTable out = new ConsoleTable();
		for (Field f : fieldList) {
			String name = f.getName();
			if (pkNames.contains(name)) name = name + "*";
			out.getHeader().add(name);
		}
		DbCollection<?> res = service.getManager().getByQualification(type, qualification, attrObj);
		
		for (Object item : res) {
			List<String> row = out.addRow();
			for (Field f : fieldList) {
				String value = String.valueOf(f.get(item));
				if (!full && value.length() > max) value = MString.truncateNice(value, max);
				row.add(value);
			}
			output = item;
		}
		res.close();
		
		out.print(System.out);
		
		if (outputParam != null)
			session.put(outputParam, output);
		return null;
	}
	

}
