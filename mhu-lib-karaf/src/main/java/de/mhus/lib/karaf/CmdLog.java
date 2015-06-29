package de.mhus.lib.karaf;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.system.ISingleton;
import de.mhus.lib.logging.level.ThreadBasedMapper;
import de.mhus.lib.logging.level.ThreadMapperConfig;
import de.mhus.lib.mutable.KarafSingletonImpl;

@Command(scope = "mhus", name = "log", description = "Manipulate Log behavior.")
public class CmdLog implements Action {

	@Argument(index=0, name="cmd", required=true, description="Command clear,add,full,dirty,level,reloadconfig,setThread [<config>],isThread,releaseThread", multiValued=false)
    String cmd;

	@Argument(index=1, name="paramteters", required=false, description="Parameters", multiValued=true)
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
			singleton.getLogFactory().updateLoggers();
			System.out.println("OK");
		} break;
		case "full": {
			singleton.setFullTrace(MCast.toboolean(parameters.length > 1 ? parameters[0] : "1", false));
			singleton.getLogFactory().updateLoggers();
			System.out.println("OK");
		} break;
		case "dirty": {
			MSingleton.setDirtyTrace(MCast.toboolean(parameters.length > 1 ? parameters[0] : "1", false));
			System.out.println("OK");
		} break;
		case "add": {
			for (String p : parameters)
				singleton.setTrace(p);
			singleton.getLogFactory().updateLoggers();
			System.out.println("OK");
		} break;
		case "list": {
			System.out.println("Default Level  : " + singleton.getLogFactory().getDefaultLevel());
			System.out.println("Trace          : " + singleton.isFullTrace());
			System.out.println("LogFoctory     : " + singleton.getLogFactory().getClass().getSimpleName());
			if (singleton.getLogFactory().getLevelMapper() != null)
			System.out.println("LevelMapper    : " + singleton.getLogFactory().getLevelMapper().getClass().getSimpleName());
			if (singleton.getLogFactory().getParameterMapper() != null)
			System.out.println("ParameterMapper: " + singleton.getLogFactory().getParameterMapper().getClass().getSimpleName());
			
			for (String name : singleton.getTraceNames())
				System.out.println(name);
		} break;
		case "reloadconfig": { //TODO need single command class
			singleton.reloadConfig();
			singleton.getLogFactory().updateLoggers();
			System.out.println("OK");
		} break;
		case "level": {
			singleton.getLogFactory().setDefaultLevel(Log.LEVEL.valueOf(parameters[0].toUpperCase()));
			singleton.getLogFactory().updateLoggers();
			System.out.println("OK");
		} break;
		case "setThread": {
			LevelMapper mapper = singleton.getLogFactory().getLevelMapper();
			if (mapper != null && mapper instanceof ThreadBasedMapper) {
				ThreadBasedMapper m = (ThreadBasedMapper)mapper;
				ThreadMapperConfig config = new ThreadMapperConfig();
				if (parameters != null && parameters.length == 1) {
					config.doConfigure(parameters[0]);
				}
				m.set(config);
			} else {
				System.out.println("Wrong Mapper " + mapper);
			}
		} break;
		case "isThread": {
			LevelMapper mapper = singleton.getLogFactory().getLevelMapper();
			if (mapper != null && mapper instanceof ThreadBasedMapper) {
				ThreadBasedMapper m = (ThreadBasedMapper)mapper;
				System.out.println("LevelMapper: " + m.get());
			} else {
				System.out.println("Wrong Mapper " + mapper);
			}
		} break;
		case "releaseThread": {
			LevelMapper mapper = singleton.getLogFactory().getLevelMapper();
			if (mapper != null && mapper instanceof ThreadBasedMapper) {
				ThreadBasedMapper m = (ThreadBasedMapper)mapper;
				m.release();
			} else {
				System.out.println("Wrong Mapper " + mapper);
			}
		} break;
		}
		
		
		return null;
	}

}
