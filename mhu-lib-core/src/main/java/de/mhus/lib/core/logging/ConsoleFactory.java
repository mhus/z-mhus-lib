package de.mhus.lib.core.logging;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.console.Console;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

public class ConsoleFactory extends LogFactory {

	//public static boolean tracing = true;
	private PrintStream out;
	private boolean traces = true;
	private boolean printTime = true;
	@SuppressWarnings("unused")
	private ResourceNode config;

	public ConsoleFactory() {
		out = System.out;
	}
	
	@Override
	public LogEngine createInstance(String name) {
		return new ConsoleLog(name);
	}

	public ConsoleFactory(IConfig config) throws Exception {
//		name = config.getExtracted("name","");
		init(config);
	}
	
	public String printTime() {
		if (printTime) {
			return MCast.toString(new Date()) + " "; // TODO maybe more efficient
		}
		return "";
	}
	
	@Override
	public void init(ResourceNode config) throws Exception {
		if (config == null) return;
		
		this.config = config;
		printTime = config.getBoolean("TIME", printTime);
		String newLevel = config.getExtracted("LEVEL",level.name());
		if (newLevel != null) level = Log.LEVEL.valueOf(newLevel.toUpperCase());
		
		String file = config.getExtracted("file",null);
		String io = config.getExtracted("stream",null);
		traces    = config.getBoolean("traces", true);
		
		if (file != null) {
			FileOutputStream f;
			try {
				f = new FileOutputStream(file,config.getBoolean("append", true));
			} catch (FileNotFoundException e) {
				throw new MException(file,e);
			}
			out = new PrintStream(f,true);
		} else
		if ("console".equals(io)) {
			out = MSingleton.get().getBaseControl().base(this).base(Console.class);
		} else
		if ("err".equals(io))
		{
			out = System.err;
		} else {
			out = System.out;
		}
		
	}
	
	public ConsoleFactory(PrintStream stream) {
		this.out = stream;
	}

//	@Override
//    public void update(Observable o, Object arg) {
//        level = Log.LEVEL.valueOf(MSingleton.instance().getConfig().getString("LEVEL",level.name()).toUpperCase()); 
//        super.update(o, arg);
//	}

	public Log.LEVEL getLevel() {
		return level;
	}

	public void setLevel(Log.LEVEL level) {
		this.level = level;
	}
	
	private class ConsoleLog extends LogEngine {
		
		private String name;

		public ConsoleLog(String name) {
			super(name);
		}
		
		@Override
		public void debug(Object message) {
			if (!isDebugEnabled()) return;
			out.println(printTime() + "DEBUG: " + name + " " + message);
			if (message != null && message instanceof Throwable)
				((Throwable)message).printStackTrace(out);
		}
	
		@Override
		public void debug(Object message, Throwable t) {
			if (!isDebugEnabled()) return;
			out.println(printTime() + "DEBUG: " + name + " " + message);
			if (t!=null && traces) t.printStackTrace(out);
		}
	
		@Override
		public void error(Object message) {
			if (!isErrorEnabled()) return;
			out.println(printTime() + "ERROR: " + name + " " + message);
			if (message != null && message instanceof Throwable && traces)
				((Throwable)message).printStackTrace(out);
		}
	
		@Override
		public void error(Object message, Throwable t) {
			if (!isErrorEnabled()) return;
			out.println(printTime() + "ERROR: " + name + " " + message);
			if (t!=null && traces) t.printStackTrace(out);
		}
	
		@Override
		public void fatal(Object message) {
			if (!isFatalEnabled()) return;
			out.println(printTime() + "FATAL: " + name + " " + message);
			if (message != null && message instanceof Throwable && traces)
				((Throwable)message).printStackTrace(out);
		}
	
		@Override
		public void fatal(Object message, Throwable t) {
			if (!isFatalEnabled()) return;
			out.println(printTime() + "FATAL: " + name + " " + message);
			if (t!=null && traces) t.printStackTrace(out);
		}
		
		@Override
		public void info(Object message) {
			if (!isInfoEnabled()) return;
			out.println(printTime() + "INFO : " + name + " " + message);
			if (message != null && message instanceof Throwable && traces)
				((Throwable)message).printStackTrace(out);
		}
	
		@Override
		public void info(Object message, Throwable t) {
			if (!isInfoEnabled()) return;
			out.println(printTime() + "INFO : " + name + " " + message);
			if (t!=null && traces) t.printStackTrace(out);
		}
	
		@Override
		public boolean isDebugEnabled() {
			return getLevel().ordinal() <= Log.LEVEL.DEBUG.ordinal();
		}
	
		@Override
		public boolean isErrorEnabled() {
			return getLevel().ordinal() <= Log.LEVEL.ERROR.ordinal();
		}
	
		@Override
		public boolean isFatalEnabled() {
			return getLevel().ordinal() <= Log.LEVEL.FATAL.ordinal();
		}
	
		@Override
		public boolean isInfoEnabled() {
			return getLevel().ordinal() <= Log.LEVEL.INFO.ordinal();
		}
	
		@Override
		public boolean isTraceEnabled() {
			return getLevel().ordinal() <= Log.LEVEL.TRACE.ordinal();
		}
	
		@Override
		public boolean isWarnEnabled() {
			return getLevel().ordinal() <= Log.LEVEL.WARN.ordinal();
		}
	
		@Override
		public void trace(Object message) {
			if (!isTraceEnabled()) return;
			out.println(printTime() + "TRACE: " + name + " " + message);
			if (message != null && message instanceof Throwable && traces)
				((Throwable)message).printStackTrace(out);
		}
	
		@Override
		public void trace(Object message, Throwable t) {
			if (!isTraceEnabled()) return;
			out.println(printTime() + "TRACE: " + name + " " + message);
			if (t!=null && traces) t.printStackTrace(out);
		}
	
		@Override
		public void warn(Object message) {
			if (!isWarnEnabled()) return;
			out.println(printTime() + "WARN : " + name + " " + message);
			if (message != null && message instanceof Throwable && traces)
				((Throwable)message).printStackTrace(out);
		}
	
		@Override
		public void warn(Object message, Throwable t) {
			if (!isWarnEnabled()) return;
			out.println(printTime() + "WARN : " + name + " " + message);
			if (t!=null && traces) t.printStackTrace(out);
		}

		@Override
		public void doInitialize(LogFactory logFactory) {
			
		}
	}
	
}
