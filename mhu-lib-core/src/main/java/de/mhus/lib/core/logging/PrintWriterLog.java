package de.mhus.lib.core.logging;

import java.io.PrintWriter;
import java.util.Date;

import de.mhus.lib.core.MCast;

/**
 * <p>PrintWriterLog class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class PrintWriterLog extends Log {

	private PrintWriter out;
	private String name;
	private boolean printTime = true;
	private Log.LEVEL level = Log.LEVEL.TRACE;
	private boolean traces = true;

	/**
	 * <p>Constructor for PrintWriterLog.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param writer a {@link java.io.PrintWriter} object.
	 */
	public PrintWriterLog(String name, PrintWriter writer) {
		super(name);
		out = writer;
		engine = new MyEngine(name);
	}
	
	private class MyEngine extends LogEngine {
		
		public MyEngine(String name) {
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
			return level.ordinal() <= LEVEL.DEBUG.ordinal();
		}
	
		@Override
		public boolean isErrorEnabled() {
			return level.ordinal() <= LEVEL.ERROR.ordinal();
		}
	
		@Override
		public boolean isFatalEnabled() {
			return level.ordinal() <= LEVEL.FATAL.ordinal();
		}
	
		@Override
		public boolean isInfoEnabled() {
			return level.ordinal() <= LEVEL.INFO.ordinal();
		}
	
		@Override
		public boolean isTraceEnabled() {
			return level.ordinal() <= LEVEL.TRACE.ordinal();
		}
	
		@Override
		public boolean isWarnEnabled() {
			return level.ordinal() <= LEVEL.WARN.ordinal();
		}
	
		@Override
		public void trace(Object message) {
			if (isTraceEnabled()) {
				out.println(printTime() + "TRACE: " + name + " " + message);
				if (message != null && message instanceof Throwable && traces)
					((Throwable)message).printStackTrace(out);
			}
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

		@Override
		public void close() {
		}
	
	}
	
	/**
	 * <p>printTime.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String printTime() {
		if (printTime) {
			return MCast.toString(new Date()) + " "; // TODO maybe more efficient
		}
		return "";
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
	 */
	public void setLevel(Log.LEVEL level) {
		this.level = level;
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
	 */
	public void setTraces(boolean traces) {
		this.traces = traces;
	}

	/**
	 * <p>getWriter.</p>
	 *
	 * @return a {@link java.io.PrintWriter} object.
	 */
	public PrintWriter getWriter() {
		return out;
	}

	/** {@inheritDoc} */
	@Override
	public void update() {
		
	}

    /** {@inheritDoc} */
    @Override
	protected void register() {
    }
    
	/** {@inheritDoc} */
	@Override
	public void unregister() {
	}

}
