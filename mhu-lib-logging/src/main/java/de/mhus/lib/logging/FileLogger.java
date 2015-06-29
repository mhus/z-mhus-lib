package de.mhus.lib.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Date;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.logging.Log;

public class FileLogger extends Log {
	
	private File file;
	private LEVEL level = LEVEL.INFO;
	private boolean printTime = true;
	private boolean traces = true;
	private PrintStream out;
	private long maxFileSize = 1024 * 1024 * 500; // 500 MB

	public FileLogger(String name, File file) {
		super(name);
		this.file = file;
	}

	public Log.LEVEL getLevel() {
		return level;
	}

	public void setLevel(Log.LEVEL level) {
		this.level = level;
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
		
		if (out != null && file.exists() && file.isFile() && file.length() > maxFileSize) {
			out.close();
			out = null;
		}
		
		if (out == null) {
			if (file.exists() && file.isFile())
				rotate();
			try {
				out = new PrintStream(file);
			} catch (FileNotFoundException e) {
				if (MSingleton.isDirtyTrace())
					e.printStackTrace();
			}
		}
		
		return out != null;
	}

	protected void rotate() {
		if (file.exists() && file.isFile())
			file.renameTo(new File(file.getParentFile(), MDate.toFileFormat(new Date()) + "." + file.getName() ));
	}

	@Override
	public boolean isDebugEnabled() {
		return getLevel().ordinal() <= LEVEL.DEBUG.ordinal();
	}

	@Override
	public boolean isErrorEnabled() {
		return getLevel().ordinal() <= LEVEL.ERROR.ordinal();
	}

	@Override
	public boolean isFatalEnabled() {
		return getLevel().ordinal() <= LEVEL.FATAL.ordinal();
	}

	@Override
	public boolean isInfoEnabled() {
		return getLevel().ordinal() <= LEVEL.INFO.ordinal();
	}

	@Override
	public boolean isTraceEnabled() {
		return getLevel().ordinal() <= LEVEL.TRACE.ordinal();
	}

	@Override
	public boolean isWarnEnabled() {
		return getLevel().ordinal() <= LEVEL.WARN.ordinal();
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
	
}
