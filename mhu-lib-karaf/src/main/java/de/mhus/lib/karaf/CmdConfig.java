package de.mhus.lib.karaf;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.console.Session;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.cfg.CfgValue;
import de.mhus.lib.core.console.ANSIConsole;
import de.mhus.lib.core.console.Console;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.core.console.Console.COLOR;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.io.TailInputStream;
import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.core.logging.TrailLevelMapper;
import de.mhus.lib.core.system.ISingleton;
import de.mhus.lib.logging.level.GeneralMapper;
import de.mhus.lib.logging.level.ThreadBasedMapper;
import de.mhus.lib.logging.level.ThreadMapperConfig;
import de.mhus.lib.mutable.KarafSingletonImpl;

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

		ISingleton s = MSingleton.get();
		if (! (s instanceof KarafSingletonImpl)) {
			System.out.println("Karaf Singleton not set");
			return null;
		}
		KarafSingletonImpl singleton = (KarafSingletonImpl)s;
		
		switch (cmd) {
		case "list": {
			ConsoleTable out = new ConsoleTable();
			out.setHeaderValues("Owner", "Path", "Value", "Default");
			for (CfgValue<?> value : MSingleton.getCfgUpdater().getList()) {
				out.addRowValues(value.getOwnerClass().getCanonicalName(), value.getPath(), value.value(), value.getDefault() );
			}
			out.print(System.out);
		} break;
		case "set": {
			for (CfgValue<?> value : MSingleton.getCfgUpdater().getList()) {
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
