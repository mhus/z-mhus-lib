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

/**
 * <p>FileLogger class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class FileLogger extends Log {
	
	protected File file;
	private Log.LEVEL level = Log.LEVEL.INFO;
	private boolean printTime = true;
	private boolean traces = true;
	private PrintStream out;
	private long maxFileSize = 1024 * 1024 * 500; // 500 MB
	private boolean autoFlush = false;
	private boolean rotate = true;

	/**
	 * <p>Constructor for FileLogger.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param file a {@link java.io.File} object.
	 */
	public FileLogger(String name, File file) {
		this(name,file,Log.LEVEL.INFO);
	}

	/**
	 * <p>Constructor for FileLogger.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param file a {@link java.io.File} object.
	 * @param autoFlush a boolean.
	 */
	public FileLogger(String name, File file, boolean autoFlush) {
		this(name,file,Log.LEVEL.INFO);
		this.autoFlush = autoFlush;
	}

	/**
	 * <p>Constructor for FileLogger.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param file a {@link java.io.File} object.
	 * @param level a {@link de.mhus.lib.core.logging.Log.LEVEL} object.
	 */
	public FileLogger(String name, File file, Log.LEVEL level) {
		super(name);
		this.level = level;
		this.file = file;
	}

	/**
	 * <p>Getter for the field <code>level</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.logging.Log.LEVEL} object.
	 */
	public Log.LEVEL getLevel() {
		return level;
	}

	/**
	 * <p>Setter for the field <code>level</code>.</p>
	 *
	 * @param level a {@link de.mhus.lib.core.logging.Log.LEVEL} object.
	 * @return a {@link de.mhus.lib.logging.FileLogger} object.
	 */
	public FileLogger setLevel(Log.LEVEL level) {
		this.level = level;
		return this;
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
	
	/**
	 * <p>print.</p>
	 *
	 * @param level a {@link java.lang.String} object.
	 * @param message a {@link java.lang.Object} object.
	 * @param t a {@link java.lang.Throwable} object.
	 */
	protected synchronized void print(String level, Object message, Throwable t) {
		if (!check()) return;
		out.println(printTime() + "," + level + "," + getInfo() + "," + message);
		
		if (message != null && message instanceof Throwable && traces)
			((Throwable)message).printStackTrace(out);
		if (t!=null && traces) t.printStackTrace(out);
		
		if (autoFlush) out.flush();
	}

	/**
	 * <p>getInfo.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected String getInfo() {
		return getName();
	}

	/**
	 * <p>check.</p>
	 *
	 * @return a boolean.
	 */
	protected synchronized boolean check() {
		
		doUpdateFile();
		
		if (file == null) return false;
		
		if (isRotate() && out != null && file.exists() && file.isFile() && file.length() > maxFileSize) {
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

	/**
	 * <p>doUpdateFile.</p>
	 */
	protected void doUpdateFile() {
	}

	/**
	 * <p>rotate.</p>
	 */
	protected void rotate() {
		if (!isRotate()) return;
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
	
	/**
	 * <p>printTime.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String printTime() {
		if (printTime) {
			return MCast.toString(new Date()); // TODO maybe more efficient
		}
		return "";
	}

	/**
	 * <p>isPrintTime.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isPrintTime() {
		return printTime;
	}

	/**
	 * <p>Setter for the field <code>printTime</code>.</p>
	 *
	 * @param printTime a boolean.
	 * @return a {@link de.mhus.lib.logging.FileLogger} object.
	 */
	public FileLogger setPrintTime(boolean printTime) {
		this.printTime = printTime;
		return this;
	}

	/**
	 * <p>isTraces.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isTraces() {
		return traces;
	}

	/**
	 * <p>Setter for the field <code>traces</code>.</p>
	 *
	 * @param traces a boolean.
	 * @return a {@link de.mhus.lib.logging.FileLogger} object.
	 */
	public FileLogger setTraces(boolean traces) {
		this.traces = traces;
		return this;
	}

	/**
	 * <p>Getter for the field <code>maxFileSize</code>.</p>
	 *
	 * @return a long.
	 */
	public long getMaxFileSize() {
		return maxFileSize;
	}

	/**
	 * <p>Setter for the field <code>maxFileSize</code>.</p>
	 *
	 * @param maxFileSize a long.
	 */
	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
	
	/** {@inheritDoc} */
	@Override
	public void update() {
		engine = new MyEngine(getName());
	}
	
	/** {@inheritDoc} */
	@Override
	public void register() {
	}
	
	/** {@inheritDoc} */
	@Override
	public void unregister() {
	}
	
	/** {@inheritDoc} */
	@Override
	public void close() {
		if (out != null) {
			out.flush();
			out.close();
			file = null;
			out = null;
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	/**
	 * <p>isRotate.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isRotate() {
		return rotate;
	}

	/**
	 * <p>Setter for the field <code>rotate</code>.</p>
	 *
	 * @param rotate a boolean.
	 * @return a {@link de.mhus.lib.logging.FileLogger} object.
	 */
	public FileLogger setRotate(boolean rotate) {
		this.rotate = rotate;
		return this;
	}

	/**
	 * <p>isAutoFlush.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isAutoFlush() {
		return autoFlush;
	}

	/**
	 * <p>Setter for the field <code>autoFlush</code>.</p>
	 *
	 * @param autoFlush a boolean.
	 * @return a {@link de.mhus.lib.logging.FileLogger} object.
	 */
	public FileLogger setAutoFlush(boolean autoFlush) {
		this.autoFlush = autoFlush;
		return this;
	}
	
	
}
