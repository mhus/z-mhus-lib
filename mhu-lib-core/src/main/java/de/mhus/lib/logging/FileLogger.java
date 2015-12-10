package de.mhus.lib.logging;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.LogEngine;
import de.mhus.lib.core.logging.LogFactory;

public class FileLogger extends Log {
	
	protected File file;
	private Log.LEVEL level = Log.LEVEL.INFO;
	private boolean printTime = true;
	private boolean traces = true;
	private PrintStream out;
	private long maxFileSize = 1024 * 1024 * 500; // 500 MB

	public FileLogger(String name, File file) {
		this(name,file,Log.LEVEL.INFO);
	}
	
	public FileLogger(String name, File file, Log.LEVEL level) {
		super(name);
		this.level = level;
		this.file = file;
	}

	public Log.LEVEL getLevel() {
		return level;
	}

	public void setLevel(Log.LEVEL level) {
		this.level = level;
	}
	
	private class MyEngine extends LogEngine {

		public MyEngine(String name) {
			super(name);
		}

			@Override
		public void debug(Object message) {
			if (!isDebugEnabled()) return;
			print("DEBUG",message,null);
		}
	
		@Override
		public void debug(Object message, Throwable t) {
			if (!isDebugEnabled()) return;
			print("DEBUG",message, t);
		}
	
		@Override
		public void error(Object message) {
			if (!isErrorEnabled()) return;
			print("ERROR",message,null);
		}
	
		@Override
		public void error(Object message, Throwable t) {
			if (!isErrorEnabled()) return;
			print("ERROR",message,t);
		}
	
		@Override
		public void fatal(Object message) {
			if (!isFatalEnabled()) return;
			print("FATAL",message,null);
		}
	
		@Override
		public void fatal(Object message, Throwable t) {
			if (!isFatalEnabled()) return;
			print("FATAL",message,t);
		}
		
		@Override
		public void info(Object message) {
			if (!isInfoEnabled()) return;
			print("INFO",message,null);
		}
	
		@Override
		public void info(Object message, Throwable t) {
			if (!isInfoEnabled()) return;
			print("INFO",message,t);
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
			if (isTraceEnabled()) {
				print("TRACE",message,null);
			}
		}

		@Override
		public void trace(Object message, Throwable t) {
			if (!isTraceEnabled()) return;
			print("TRACE",message,t);
		}

		@Override
		public void warn(Object message) {
			if (!isWarnEnabled()) return;
			print("WARN",message,null);
		}

		@Override
		public void warn(Object message, Throwable t) {
			if (!isWarnEnabled()) return;
			print("WARN",message,t);
		}

		@Override
		public void doInitialize(LogFactory logFactory) {
		}

		@Override
		public void close() {
		}
		
	}
	
	protected synchronized void print(String level, Object message, Throwable t) {
		if (!check()) return;
		out.println(printTime() + "," + level + "," + getInfo() + "," + message);
		
		if (message != null && message instanceof Throwable && traces)
			((Throwable)message).printStackTrace(out);
		if (t!=null && traces) t.printStackTrace(out);
		
	}

	protected String getInfo() {
		return getName();
	}

	protected synchronized boolean check() {
		
		doUpdateFile();
		
		if (file == null) return false;
		
		if (out != null && file.exists() && file.isFile() && file.length() > maxFileSize) {
			out.flush();
			out.close();
			out = null;
		}
		
		if (out == null) {
			if (file.exists() && file.isFile())
				rotate();
			try {
				out = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)));
			} catch (FileNotFoundException e) {
				if (MSingleton.isDirtyTrace())
					e.printStackTrace();
			}
		}
		
		return out != null;
	}

	protected void doUpdateFile() {
	}

	protected void rotate() {
		File oldFile = file;
		doUpdateFile();
		if (file == null) {
			if (oldFile != null && out != null) {
				out.close();
				out = null;
			}
			return;
		}
		if (file.exists() && file.isFile())
			file.renameTo(new File(file.getParentFile(), MDate.toFileFormat(new Date()) + "." + file.getName() ));
	}
	
	public String printTime() {
		if (printTime) {
			return MCast.toString(new Date()); // TODO maybe more efficient
		}
		return "";
	}

	public boolean isPrintTime() {
		return printTime;
	}

	public void setPrintTime(boolean printTime) {
		this.printTime = printTime;
	}

	public boolean isTraces() {
		return traces;
	}

	public void setTraces(boolean traces) {
		this.traces = traces;
	}

	public long getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
	
	@Override
	public void update() {
		engine = new MyEngine(getName());
	}
	
	@Override
	public void register() {
	}
	
	@Override
	public void unregister() {
	}
	
	@Override
	public void close() {
		if (out != null) {
			out.flush();
			out.close();
			file = null;
			out = null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}
	
	
}
