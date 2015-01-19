package de.mhus.lib.karaf;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.system.ISingleton;
import de.mhus.lib.mutable.KarafSingletonImpl;

@Command(scope = "mhus", name = "log", description = "Manipulate Log behavior.")
public class CmdLog implements Action {

	@Argument(index=0, name="cmd", required=true, description="Command clear,add,full", multiValued=false)
    String cmd;

	@Argument(index=3, name="paramteters", required=false, description="Parameters", multiValued=true)
    String[] parameters;

	@Override
	public Object execute(CommandSession session) throws Exception {

		ISingleton s = MSingleton.get();
		if (! (s instanceof KarafSingletonImpl)) {
			System.out.println("Karaf Singleton not set");
			return null;
		}
		KarafSingletonImpl singleton = (KarafSingletonImpl)s;
		
		switch (cmd) {
		case "clear": {
			singleton.clearTrace();
			singleton.setFullTrace(false);
			System.out.println("OK");
		} break;
		case "full": {
			singleton.setFullTrace(MCast.toboolean(parameters.length > 1 ? parameters[0] : "1", false));
			System.out.println("OK");
		} break;
		case "add": {
			for (String p : parameters)
				singleton.setTrace(p);
			System.out.println("OK");
		} break;
		case "list": {
			for (String name : singleton.getTraceNames())
				System.out.println(name);
		} break;
		case "reloadconfig": { //TODO need single command class
			singleton.reloadConfig();
		} break;
		}
		
		
		return null;
	}

}
