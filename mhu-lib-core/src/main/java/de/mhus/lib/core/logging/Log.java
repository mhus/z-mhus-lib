package de.mhus.lib.core.logging;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.util.Stringifier;

/**
 * Got the interface from apache-commons-logging. I need to switch because its not working
 * in eclipse plugins correctly.
 * 
 * @author mikehummel
 *
 */
public abstract class Log {

	public enum LEVEL {TRACE,DEBUG,INFO,WARN,ERROR,FATAL};

	protected boolean localTrace = true;
	private String name;
	protected LevelMapper levelMapper;
    private ParameterMapper parameterMapper;
	
	public Log(String name) {
		this.name = name;
		localTrace = MSingleton.isTrace(name);
	}

    /**
     * Convenience method to return a named logger, without the application
     * having to care about factories.
     *
     * @param clazz Class from which a log name will be derived
     */
//    public static Log getLog(Class<?> clazz) {
//
//    	Log log;
//		try {
//			log = MSingleton.instance().getLogFactory(clazz).getInstance(clazz);
//		} catch (Exception e) {
//			log = new ConsoleFactory();
//		}
//		log.name = clazz.getCanonicalName();
//		log.update(null, null);
//		MSingleton.instance().registerConfigListener(log);
//		return log;
//    }

    /**
     * Convenience method to return a named logger, without the application
     * having to care about factories.
     *
     * @param name Logical name of the <code>Log</code> instance to be
     *  returned (the meaning of this name is only known to the underlying
     *  logging implementation that is being wrapped)
     */
//    public static Log getLog(String name) {
//
//    	Log log;
//		try {
//			log = MSingleton.instance().getLogFactory(name).getInstance(name);
//		} catch (Exception e) {
//			log = new ConsoleFactory();
//		}
//        log.name = name;
//        log.update(null, null);
//        MSingleton.instance().registerConfigListener(log);
//        return log;
//    }
    
    /**
     * <p> Is debug logging currently enabled? </p>
     *
     * <p> Call this method to prevent having to perform expensive operations
     * (for example, <code>String</code> concatenation)
     * when the log level is more than debug. </p>
     * @return 
     */
    public abstract boolean isDebugEnabled();


    /**
     * <p> Is error logging currently enabled? </p>
     *
     * <p> Call this method to prevent having to perform expensive operations
     * (for example, <code>String</code> concatenation)
     * when the log level is more than error. </p>
     * @return 
     */
    public abstract boolean isErrorEnabled();


    /**
     * <p> Is fatal logging currently enabled? </p>
     *
     * <p> Call this method to prevent having to perform expensive operations
     * (for example, <code>String</code> concatenation)
     * when the log level is more than fatal. </p>
     * @return 
     */
    public abstract boolean isFatalEnabled();


    /**
     * <p> Is info logging currently enabled? </p>
     *
     * <p> Call this method to prevent having to perform expensive operations
     * (for example, <code>String</code> concatenation)
     * when the log level is more than info. </p>
     * @return 
     */
    public abstract boolean isInfoEnabled();


    /**
     * <p> Is trace logging currently enabled? </p>
     *
     * <p> Call this method to prevent having to perform expensive operations
     * (for example, <code>String</code> concatenation)
     * when the log level is more than trace. </p>
     * @return 
     */
    public abstract boolean isTraceEnabled();


    /**
     * <p> Is warn logging currently enabled? </p>
     *
     * <p> Call this method to prevent having to perform expensive operations
     * (for example, <code>String</code> concatenation)
     * when the log level is more than warn. </p>
     * @return 
     */
    public abstract boolean isWarnEnabled();


    // -------------------------------------------------------- Logging Methods
    
    /**
     * Log a message in trace, it will automatically append the objects if trace is enabled. Can Also add a trace.
     * This is the local trace method. The trace will only written if the local trace is switched on.
     * @param msg 
     */
    public void t(Object ... msg) {
    	log(LEVEL.TRACE, msg);
    }

    public void log(LEVEL level, Object ... msg) {
    	
    	if (levelMapper != null) level = levelMapper.map(this, level,msg);
    	if (parameterMapper != null) msg = parameterMapper.map(this, msg);
    	
    	switch (level) {
		case DEBUG:
			if (!isDebugEnabled()) return;
			break;
		case ERROR:
			if (!isErrorEnabled()) return;
			break;
		case FATAL:
			if (!isFatalEnabled()) return;
			break;
		case INFO:
			if (!isInfoEnabled()) return;
			break;
		case TRACE:
			if (!isTraceEnabled()) return;
			break;
		case WARN:
			if (!isWarnEnabled()) return;
			break;
		default:
			return;
    	}

    	StringBuffer sb = new StringBuffer();
    	prepare(sb);
    	Throwable error = null;
//    	int cnt=0;
    	for (Object o : msg) {
			error = serialize(sb,o, error);
//   		cnt++;
    	}
    	
    	switch (level) {
		case DEBUG:
	    	debug(sb.toString(),error);
			break;
		case ERROR:
	    	error(sb.toString(),error);
			break;
		case FATAL:
	    	fatal(sb.toString(),error);
			break;
		case INFO:
	    	info(sb.toString(),error);
			break;
		case TRACE:
	    	trace(sb.toString(),error);
			break;
		case WARN:
	    	warn(sb.toString(),error);
			break;
		default:
			break;
    	}
	}

	private Throwable serialize(StringBuffer sb, Object o, Throwable error) {
    	try {
	    	if (o == null) {
				sb.append("[null]");
	    	} else
			if (o instanceof Throwable) {
				if (error == null) return (Throwable)o;
				// another error
				sb.append("[").append(o).append("]");
			} else
	    	if (o.getClass().isArray()) {
	    		sb.append("{");
	    		for (Object p : (Object[])o) {
	    			error = serialize(sb, p, error);
	    		}
	    		sb.append("}");
	    	} else
	    		sb.append("[").append(o).append("]");
    	} catch (Throwable t) {}
		return error;
	}


//	/**
//     * Log a message in trace, it will automatically append the objects if trace is enabled. Can Also add a trace.
//     */
//    public void tt(Object ... msg) {
//    	if (!isTraceEnabled()) return;
//    	StringBuffer sb = new StringBuffer();
//    	prepare(sb);
//    	Throwable error = null;
////    	int cnt=0;
//    	for (Object o : msg) {
//			error = serialize(sb,o, error);
////    		cnt++;
//    	}
//    	trace(sb.toString(),error);
//    }

    /**
     * Log a message in debug, it will automatically append the objects if debug is enabled. Can Also add a trace.
     * @param msg 
     */
    public void d(Object ... msg) {
    	log(LEVEL.DEBUG, msg);
    }

    /**
     * Log a message in info, it will automatically append the objects if debug is enabled. Can Also add a trace.
     * @param msg 
     */
    public void i(Object ... msg) {
    	log(LEVEL.INFO, msg);
    }
    
    /**
     * Log a message in warn, it will automatically append the objects if debug is enabled. Can Also add a trace.
     * @param msg 
     */
    public void w(Object ... msg) {
    	log(LEVEL.WARN, msg);
    }

    /**
     * Log a message in error, it will automatically append the objects if debug is enabled. Can Also add a trace.
     * @param msg 
     */
    public void e(Object ... msg) {
    	log(LEVEL.ERROR, msg);
    }

    /**
     * Log a message in info, it will automatically append the objects if debug is enabled. Can Also add a trace.
     * @param msg 
     */
    public void f(Object ... msg) {
    	log(LEVEL.FATAL, msg);
    }

    protected void prepare(StringBuffer sb) {
    	if (levelMapper != null) {
    		levelMapper.prepareMessage(this,sb);
    	} else {
    		sb.append('[').append(Thread.currentThread().getId()).append(']');
    	}
	}

	/**
     * <p> Log a message with trace log level. </p>
     *
     * @param message log this message
     */
    protected abstract void trace(Object message);


    /**
     * <p> Log an error with trace log level. </p>
     *
     * @param message log this message
     * @param t log this cause
     */
    protected abstract void trace(Object message, Throwable t);


    /**
     * <p> Log a message with debug log level. </p>
     *
     * @param message log this message
     */
    protected abstract void debug(Object message);


    /**
     * <p> Log an error with debug log level. </p>
     *
     * @param message log this message
     * @param t log this cause
     */
    protected abstract void debug(Object message, Throwable t);


    /**
     * <p> Log a message with info log level. </p>
     *
     * @param message log this message
     */
    protected abstract void info(Object message);


    /**
     * <p> Log an error with info log level. </p>
     *
     * @param message log this message
     * @param t log this cause
     */
    protected abstract void info(Object message, Throwable t);


    /**
     * <p> Log a message with warn log level. </p>
     *
     * @param message log this message
     */
    protected abstract void warn(Object message);


    /**
     * <p> Log an error with warn log level. </p>
     *
     * @param message log this message
     * @param t log this cause
     */
    protected abstract void warn(Object message, Throwable t);


    /**
     * <p> Log a message with error log level. </p>
     *
     * @param message log this message
     */
    protected abstract void error(Object message);


    /**
     * <p> Log an error with error log level. </p>
     *
     * @param message log this message
     * @param t log this cause
     */
    protected abstract void error(Object message, Throwable t);


    /**
     * <p> Log a message with fatal log level. </p>
     *
     * @param message log this message
     */
    protected abstract void fatal(Object message);


    /**
     * <p> Log an error with fatal log level. </p>
     *
     * @param message log this message
     * @param t log this cause
     */
    protected abstract void fatal(Object message, Throwable t);

	public void setTrace(boolean localTrace) {
		this.localTrace = localTrace;
	}

	public boolean isTrace() {
		return localTrace;
	}

	/**
	 * Use the name of the caller
	 * @return
	 */
//	public static Log getLog() {
//		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
//		// for (StackTraceElement e : stack) System.out.println(e.getClassName());
//		return getLog(stack[2].getClassName());
//	}


	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return MSystem.toString(this, getName());
	}
	
	public static Log getLog(Object owner) {
		// return new StaticBase(owner).log();
		return MSingleton.get().createLog(owner);
	}

	public void update() {
		localTrace = MSingleton.isTrace(name);
		levelMapper = MSingleton.get().getLogFactory().getLevelMapper();
		parameterMapper = MSingleton.get().getLogFactory().getParameterMapper();
	}

	public ParameterMapper getParameterMapper() {
		return parameterMapper;
	}

	public void doInitialize(LogFactory logFactory) {
		levelMapper = logFactory.getLevelMapper();
		parameterMapper = logFactory.getParameterMapper();
	}
	
	/**
	 * Return if the given level is enabled. This function also uses the
	 * levelMapper to find the return value. Instead of the is...Enabled().
	 * 
	 * @param level
	 * @return
	 */
	public boolean isLevelEnabled(LEVEL level) {
    	if (levelMapper != null) level = levelMapper.map(this, level);
    	
    	switch (level) {
		case DEBUG:
			return isDebugEnabled();
		case ERROR:
			return isErrorEnabled();
		case FATAL:
			return isFatalEnabled();
		case INFO:
			return isInfoEnabled();
		case TRACE:
			return isTraceEnabled();
		case WARN:
			return isWarnEnabled();
		default:
			return false;
    	}

	}
	
}
