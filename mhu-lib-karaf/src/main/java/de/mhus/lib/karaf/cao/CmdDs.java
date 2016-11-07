package de.mhus.lib.karaf.cao;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.cao.CaoDataSource;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.karaf.MOsgi;

@Command(scope = "cao", name = "ds-list", description = "List All CAO Datasources")
@Service
public class CmdDs implements Action {

	@Override
	public Object execute() throws Exception {
		ConsoleTable out = new ConsoleTable();
		out.setHeaderValues("Name","Type","Status");
		for (CaoDataSource ds : MOsgi.getServices(CaoDataSource.class, null)) {
			out.addRowValues(ds.getName(), ds.getType(), ds);
		}
		out.print(System.out);
		return null;
	}
	
}
