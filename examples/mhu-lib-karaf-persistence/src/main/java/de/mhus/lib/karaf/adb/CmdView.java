package de.mhus.lib.karaf.adb;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;

import de.mhus.lib.adb.model.Field;
import de.mhus.lib.adb.model.Table;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.console.ConsoleTable;

@Command(scope = "adb", name = "view", description = "Show a object")
public class CmdView implements Action {

	@Argument(index=0, name="service", required=true, description="Service Class", multiValued=false)
    String serviceName;

	@Argument(index=1, name="type", required=true, description="Type to select", multiValued=false)
    String typeName;
	
	@Argument(index=2, name="id", required=false, description="Id of the object or query in brakets e.g '($db.table.field$ = 1)'", multiValued=false)
    String id;
	
	@Option(name="-o", aliases="--out", description="Comma separated list of fields to print",required=false)
	String fieldsComma = null;

	@Option(name="-f", aliases="--full", description="Print the full value content also if it's very long",required=false)
	boolean full = false;

	@Option(name="-m", aliases="--max", description="Maximum amount of chars for a value (if not full)",required=false)
	int max = 40;

	@Option(name="-x", description="Output parameter",required=false)
	String outputParam = null;

	@Override
	public Object execute(CommandSession session) throws Exception {
		
		Object output = null;
		
		DbManagerService service = AdbUtil.getService(serviceName);
		Class<?> type = AdbUtil.getType(service, typeName);
		
		String regName = service.getManager().getRegistryName(type);
		Table tableInfo = service.getManager().getTable(regName);
		
		ConsoleTable out = new ConsoleTable();
		out.setHeaderValues("Field","Value","Type");
		
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
		
		for (Object object : AdbUtil.getObjects(service, type, id)) {

			System.out.println(">>> VIEW " + object);
			
			
			for (Field f : fieldList) {
				String name = f.getName();
				if (pkNames.contains(name)) name = name + "*";
				String value = String.valueOf(f.get(object));
				if (!full && value.length() > max) value = MString.truncateNice(value, max);
				out.addRowValues(name,  value, f.getType().getSimpleName() );
			}
			out.print(System.out);
			output = object;
		}
		
		if (outputParam != null)
			session.put(outputParam, output);
		return null;
	}
	

}
