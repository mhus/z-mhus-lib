package de.mhus.lib.karaf;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.console.Session;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.cfg.CfgValue;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.core.system.IApi;
import de.mhus.lib.mutable.KarafMApiImpl;

@Command(scope = "mhus", name = "config", description = "Manipulate Configuration Values")
@Service
public class CmdConfig extends MLog implements Action {

    @Reference
    private Session session;

	@Argument(index=0, name="cmd", required=true, description="Command:\n list, set <owner> <path> <value>", multiValued=false)
    String cmd;

	@Argument(index=1, name="paramteters", required=false, description="Parameters", multiValued=true)
    String[] parameters;

	// private Appender appender;

	@Override
	public Object execute() throws Exception {

		IApi s = MApi.get();
		if (! (s instanceof KarafMApiImpl)) {
			System.out.println("Karaf MApi not set");
			return null;
		}
		KarafMApiImpl api = (KarafMApiImpl)s;
		
		switch (cmd) {
		case "list": {
			ConsoleTable out = new ConsoleTable();
			out.setHeaderValues("Owner", "Path", "Value", "Default");
			for (CfgValue<?> value : MApi.getCfgUpdater().getList()) {
				out.addRowValues(value.getOwnerClass().getCanonicalName(), value.getPath(), value.value(), value.getDefault() );
			}
			out.print(System.out);
		} break;
		case "set": {
			for (CfgValue<?> value : MApi.getCfgUpdater().getList()) {
				if (value.getOwnerClass().getCanonicalName().equals(parameters[0]) && value.getPath().equals(parameters[1])) {
					value.setValue(parameters[2]);
					System.out.println("OK");
					break;
				}
			}

		}
		}
		
		
		return null;
	}

}
