package de.mhus.lib.karaf.services;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

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
			table.setHeaderValues("Name","Size","Enabled","Status");
			for (CacheControlIfc c : MOsgi.getServices(CacheControlIfc.class, null))
				try {
					table.addRowValues(c.getName(), c.getSize(),c.isEnabled(), "ok");
				} catch (Throwable t) {
					log().d(c,t);
					table.addRowValues(c.getName(), "","", t.getMessage());
				}
			table.print(System.out);
		} else
		if (cmd.equals("clear")) {
			String name = null;
			if (parameters != null && parameters.length > 0)
				name = parameters[0];
			CacheControlUtil.clear(name);
			System.out.println("OK");
			
		} else
		if (cmd.equals("enable")) {
			String name = null;
			if (parameters != null && parameters.length > 0)
				name = parameters[0];
			CacheControlUtil.enable(name, true);
			System.out.println("OK");
			
		} else
		if (cmd.equals("disable")) {
			String name = null;
			if (parameters != null && parameters.length > 0)
				name = parameters[0];
			CacheControlUtil.enable(name, false);
			System.out.println("OK");
			
		}

		return null;
	}
	
}
