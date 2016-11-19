package de.mhus.lib.karaf.services;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.karaf.MOsgi;

@Command(scope = "mhus", name = "cache", description = "Cache Control Service Control")
@Service
public class CmdCacheControl extends MLog implements Action {

	@Argument(index=0, name="cmd", required=true, description="list,clear", multiValued=false)
    String cmd;

	@Argument(index=1, name="paramteters", required=false, description="Parameters", multiValued=true)
    String[] parameters;

	@Override
	public Object execute() throws Exception {
		
		if (cmd.equals("list")) {
			ConsoleTable table = new ConsoleTable();
			table.setHeaderValues("Name","Size");
			for (CacheControlIfc c : MOsgi.getServices(CacheControlIfc.class, null))
				table.addRowValues(c.getName(), c.getSize());
			table.print(System.out);
		} else
		if (cmd.equals("clear")) {
			String name = null;
			if (parameters != null && parameters.length > 0)
				name = parameters[0];
			CacheControlUtil.clear(name);
			System.out.println("OK");
			
		}

		return null;
	}
	
}
