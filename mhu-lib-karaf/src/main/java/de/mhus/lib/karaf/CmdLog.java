package de.mhus.lib.karaf;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.core.logging.TrailLevelMapper;
import de.mhus.lib.core.system.ISingleton;
import de.mhus.lib.logging.level.GeneralMapper;
import de.mhus.lib.logging.level.ThreadBasedMapper;
import de.mhus.lib.logging.level.ThreadMapperConfig;
import de.mhus.lib.mutable.KarafSingletonImpl;

@Command(scope = "mhus", name = "log", description = "Manipulate Log behavior.")
@Service
public class CmdLog extends MLog implements Action {

	@Argument(index=0, name="cmd", required=true, description="Command:\n clear - reset all loggers,\n add <path> - add a trace log,\n full - enable full trace logging,\n dirty - enable dirty logging,\n level - set log level (console logger),\n reloadconfig,\n settrail [<config>] - enable trail logging for this thread,\n istrail - output the traillog config,\n releasetrail - unset the current trail log config\n general - enable general logging\n off - log mapping off\n trace,debug,info,warn,error,fatal <msg>", multiValued=false)
    String cmd;

	@Argument(index=1, name="paramteters", required=false, description="Parameters", multiValued=true)
    String[] parameters;

	@Override
	public Object execute() throws Exception {

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
			MSingleton.updateLoggers();
			System.out.println("OK");
		} break;
		case "full": {
			singleton.setFullTrace(MCast.toboolean(parameters.length >= 1 ? parameters[0] : "1", false));
			MSingleton.updateLoggers();
			System.out.println("OK");
		} break;
		case "dirty": {
			MSingleton.setDirtyTrace(MCast.toboolean(parameters.length >= 1 ? parameters[0] : "1", false));
			System.out.println("OK");
		} break;
		case "add": {
			for (String p : parameters)
				singleton.setTrace(p);
			MSingleton.updateLoggers();
			System.out.println("OK");
		} break;
		case "list": {
			System.out.println("Default Level  : " + singleton.getLogFactory().getDefaultLevel());
			System.out.println("Trace          : " + singleton.isFullTrace());
			System.out.println("LogFoctory     : " + singleton.getLogFactory().getClass().getSimpleName());
			System.out.println("DirtyTrace     : " + MSingleton.isDirtyTrace());
			LevelMapper lm = singleton.getLogFactory().getLevelMapper();
			if (lm != null) {
			System.out.println("LevelMapper    : " + lm.getClass().getSimpleName());
			if (lm instanceof TrailLevelMapper)
			System.out.println("   Configurtion: " + ((TrailLevelMapper)lm).doSerializeTrail() );
			}
			if (singleton.getLogFactory().getParameterMapper() != null)
			System.out.println("ParameterMapper: " + singleton.getLogFactory().getParameterMapper().getClass().getSimpleName());
			
			for (String name : singleton.getTraceNames())
				System.out.println(name);
		} break;
		case "reloadconfig": { //TODO need single command class
			singleton.getCfgManager().reConfigure();
			MSingleton.updateLoggers();
			System.out.println("OK");
		} break;
		case "level": {
			singleton.getLogFactory().setDefaultLevel(Log.LEVEL.valueOf(parameters[0].toUpperCase()));
			MSingleton.updateLoggers();
			System.out.println("OK");
		} break;
		case "settrail": {
			LevelMapper mapper = singleton.getLogFactory().getLevelMapper();
			if (MLogUtil.isTrailLevelMapper()) {
				MLogUtil.setTrailConfig(parameters == null || parameters.length < 1 ? "" : parameters[0]);
				System.out.println("Trail Config: " + MLogUtil.getTrailConfig() );
			} else {
				System.out.println("Wrong Mapper " + mapper);
			}
		} break;
		case "istrail": {
			LevelMapper mapper = singleton.getLogFactory().getLevelMapper();
			if (MLogUtil.isTrailLevelMapper()) {
				System.out.println("LevelMapper: " + MLogUtil.getTrailConfig());
			} else {
				System.out.println("Wrong Mapper " + mapper);
			}
		} break;
		case "releasetrail": {
			LevelMapper mapper = singleton.getLogFactory().getLevelMapper();
			if (MLogUtil.isTrailLevelMapper()) {
				MLogUtil.releaseTrailConfig();
				System.out.println("OK");
			} else {
				System.out.println("Wrong Mapper " + mapper);
			}
		} break;
		case "general": {
			ThreadMapperConfig config = new ThreadMapperConfig();
			config.doConfigure(parameters == null || parameters.length < 1 ? "" : parameters[0]);
			GeneralMapper mapper = new GeneralMapper();
			mapper.setConfig(config);
			singleton.getLogFactory().setLevelMapper(mapper);
			System.out.println("Sel Global Mapper: OK " + singleton.getLogFactory().getLevelMapper() + " " + config.getTrailId());
		} break;
		case "trail": {
			singleton.getLogFactory().setLevelMapper(new ThreadBasedMapper());
			System.out.println("Set Trail Mapper OK " + singleton.getLogFactory().getLevelMapper() );
		} break;
		case "off": {
			singleton.getLogFactory().setLevelMapper(null);
			System.out.println("Remove Mapper OK " + singleton.getLogFactory().getLevelMapper() );
		} break;
		case "trace": {
			log().t((Object[])parameters);
		} break;
		case "debug": {
			log().d((Object[])parameters);
		} break;
		case "info": {
			log().i((Object[])parameters);
		} break;
		case "warn": {
			log().w((Object[])parameters);
		} break;
		case "error": {
			log().e((Object[])parameters);
		} break;
		case "fatal": {
			log().f((Object[])parameters);
		} break;
		}
		
		
		return null;
	}

}
