/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.logging;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import io.opentracing.Span;

/**
 * Got the interface from apache-commons-logging. I need to switch because its not working in
 * eclipse plugins correctly.
 *
 * @author mikehummel
 */
public class Log {

    public enum LEVEL {
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        FATAL
    };

    private static ThreadLocal<String> lookingForSpan = new ThreadLocal<>();
    protected boolean localTrace = true;
    private static boolean stacktraceTrace = false;
    protected String name;
    protected static ParameterMapper parameterMapper;
    protected LogEngine engine = null;
    private List<String> maxMsgSizeExceptions;
	private ITracer tracer;
	private boolean tracerInError = false;
    //    protected UUID id = UUID.randomUUID();
    private static int maxMsgSize = 0;
    private static boolean verbose = false;

    public Log(Object owner) {

        name = MSystem.getOwnerName(owner);
        localTrace = MApi.isTrace(name);
//        tracer = getITracer(); - causes stack loop

        update();

        //		register();
    }

    //    protected void register() {
    //		MApi.registerLogger(this);
    //	}
    //
    //    protected void unregister() {
    //		MApi.unregisterLogger(this);
    //    }

    // -------------------------------------------------------- Logging Methods

    /**
     * Log a message in trace, it will automatically append the objects if trace is enabled. Can
     * Also add a trace. This is the local trace method. The trace will only written if the local
     * trace is switched on.
     *
     * @param msg
     */
    public void t(Object... msg) {
        log(LEVEL.TRACE, msg);
    }

    public void log(LEVEL level, Object... msg) {
        if (engine == null) return;

        // level mapping
        Span span = null;
        // avoid stack overload
        if (lookingForSpan.get() == null) {
        	try {
        		lookingForSpan.set("");
            	span = getITracer().current();
        	} catch (Throwable t) {
        	    System.out.println("*** Error looking for ITracer span: " + name + " " + t);
            } finally {
            	lookingForSpan.remove();
            }
        }
        if (verbose) {
            if (level == LEVEL.DEBUG)
                level = LEVEL.INFO;
        }
        if (span != null) {
            String mapping = span.getBaggageItem(MLog.LOG_LEVEL_MAPPING);
            if (mapping != null) {
                switch (mapping) {
                    case "trace":
                        if (level == LEVEL.TRACE) level = LEVEL.INFO;
                    case "debug":
                        if (level == LEVEL.DEBUG) level = LEVEL.INFO;
                }
            }
        }

        switch (level) {
            case DEBUG:
                if (!engine.isDebugEnabled()) return;
                break;
            case ERROR:
                if (!engine.isErrorEnabled()) return;
                break;
            case FATAL:
                if (!engine.isFatalEnabled()) return;
                break;
            case INFO:
                if (!engine.isInfoEnabled()) return;
                break;
            case TRACE:
                if (!engine.isTraceEnabled()) return;
                break;
            case WARN:
                if (!engine.isWarnEnabled()) return;
                break;
            default:
                return;
        }

        if (parameterMapper != null) msg = parameterMapper.map(this, msg);

        StringBuilder sb = new StringBuilder();
        prepare(sb);
        Throwable error = MString.serialize(sb, msg, maxMsgSize, maxMsgSizeExceptions);

        switch (level) {
            case DEBUG:
                engine.debug(sb.toString(), error);
                break;
            case ERROR:
                engine.error(sb.toString(), error);
                break;
            case FATAL:
                engine.fatal(sb.toString(), error);
                break;
            case INFO:
                engine.info(sb.toString(), error);
                break;
            case TRACE:
                engine.trace(sb.toString(), error);
                break;
            case WARN:
                engine.warn(sb.toString(), error);
                break;
            default:
                break;
        }

        if (stacktraceTrace) {
            String stacktrace =
                    MCast.toString("stacktracetrace", Thread.currentThread().getStackTrace());
            engine.debug(stacktrace);
        }
    }

    private ITracer getITracer() {
    	if (!tracerInError) {
	    	try {
	    		tracer = ITracer.get();
	    	} catch (ClassCastException e) {
	    		// The exception occurs if because of class loader conflicts the tracer is no more compatible to the interface
	    		// in this case use the last loaded for further actions
	    		tracerInError = true;
	    		System.out.println(new Date() + " ERROR: 1 ITRACER IN ERROR " + e + ", ClassLoader: " + getClass().getClassLoader() + ", Log: " + name );
	    		if (tracer == null)
	    			System.out.println(new Date() + " FATAL: *** CAN'T LOAD ITRACER FOR: " + name);
	    	} catch (Throwable t) {
                System.out.println(new Date() + " ERROR: 2 ITRACER IN ERROR " + t + ", ClassLoader: " + getClass().getClassLoader() + ", Log: " + name );
	    	}
	    	
    	}
		return tracer;
	}

	// toos from MDate
    protected static String toIsoDateTime(Date _in) {
        Calendar c = Calendar.getInstance();
        c.setTime(_in);
        return toIsoDateTime(c);
    }

    protected static String toIsoDateTime(Calendar _in) {
        return _in.get(Calendar.YEAR)
                + "-"
                + toDigits(_in.get(Calendar.MONTH) + 1, 2)
                + "-"
                + toDigits(_in.get(Calendar.DAY_OF_MONTH), 2)
                + " "
                + toDigits(_in.get(Calendar.HOUR_OF_DAY), 2)
                + ":"
                + toDigits(_in.get(Calendar.MINUTE), 2)
                + ":"
                + toDigits(_in.get(Calendar.SECOND), 2);
    }

    protected static String toDigits(int _in, int _digits) {
        StringBuilder out = new StringBuilder().append(Integer.toString(_in));
        while (out.length() < _digits) out.insert(0, '0');
        return out.toString();
    }

    //	/**
    //     * Log a message in trace, it will automatically append the objects if trace is enabled.
    // Can Also add a trace.
    //     */
    //    public void tt(Object ... msg) {
    //    	if (!isTraceEnabled()) return;
    //    	StringBuilder sb = new StringBuilder();
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
     * Log a message in debug, it will automatically append the objects if debug is enabled. Can
     * Also add a trace.
     *
     * @param msg
     */
    public void d(Object... msg) {
        log(LEVEL.DEBUG, msg);
    }

    /**
     * Log a message in info, it will automatically append the objects if debug is enabled. Can Also
     * add a trace.
     *
     * @param msg
     */
    public void i(Object... msg) {
        log(LEVEL.INFO, msg);
    }

    /**
     * Log a message in warn, it will automatically append the objects if debug is enabled. Can Also
     * add a trace.
     *
     * @param msg
     */
    public void w(Object... msg) {
        log(LEVEL.WARN, msg);
    }

    /**
     * Log a message in error, it will automatically append the objects if debug is enabled. Can
     * Also add a trace.
     *
     * @param msg
     */
    public void e(Object... msg) {
        log(LEVEL.ERROR, msg);
    }

    /**
     * Log a message in info, it will automatically append the objects if debug is enabled. Can Also
     * add a trace.
     *
     * @param msg
     */
    public void f(Object... msg) {
        log(LEVEL.FATAL, msg);
    }

    protected void prepare(StringBuilder sb) {
        sb.append('[').append(Thread.currentThread().getId()).append(']');
    }

    public void setLocalTrace(boolean localTrace) {
        this.localTrace = localTrace;
    }

    public boolean isLocalTrace() {
        return localTrace;
    }

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
        try {
            return MApi.get().lookupLog(owner);
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    public void update() {
        engine = MApi.get().getLogFactory().getInstance(getName());
        localTrace = MApi.isTrace(name);
        parameterMapper = MApi.get().getLogFactory().getParameterMapper();
        maxMsgSize = MApi.get().getLogFactory().getMaxMessageSize();
        maxMsgSizeExceptions = MApi.get().getLogFactory().getMaxMessageSizeExceptions();
    }

    public ParameterMapper getParameterMapper() {
        return parameterMapper;
    }

    /**
     * Return if the given level is enabled. This function also uses the levelMapper to find the
     * return value. Instead of the is...Enabled().
     *
     * @param level
     * @return true if level is enabled
     */
    public boolean isLevelEnabled(LEVEL level) {
        if (engine == null) return false;

        if (localTrace) level = LEVEL.INFO;

        // level mapping
        Span span = null;
        // avoid stack overload
        if (lookingForSpan.get() == null) {
        	try {
        		lookingForSpan.set("");
            	span = getITracer().current();
            } finally {
            	lookingForSpan.remove();
            }
        }
        if (span != null) {
            String mapping = span.getBaggageItem(MLog.LOG_LEVEL_MAPPING);
            if (mapping != null) {
                switch (mapping) {
                    case "trace":
                        if (level == LEVEL.TRACE) return engine.isInfoEnabled();
                    case "debug":
                        if (level == LEVEL.DEBUG) return engine.isInfoEnabled();
                }
            }
        }

        switch (level) {
            case DEBUG:
                return engine.isDebugEnabled();
            case ERROR:
                return engine.isErrorEnabled();
            case FATAL:
                return engine.isFatalEnabled();
            case INFO:
                return engine.isInfoEnabled();
            case TRACE:
                return engine.isTraceEnabled();
            case WARN:
                return engine.isWarnEnabled();
            default:
                return false;
        }
    }

    public void close() {
        if (engine == null) return;
        //		unregister();
        engine.close();
        engine = null;
    }

    public static boolean isStacktraceTrace() {
        return stacktraceTrace;
    }

    public static void setStacktraceTrace(boolean stacktraceTrace) {
        Log.stacktraceTrace = stacktraceTrace;
    }

    public static boolean isVerbose() {
        return verbose;
    }

    public static void setVerbose(boolean verbose) {
        Log.verbose = verbose;
    }

    public static int getMaxMsgSize() {
        return maxMsgSize;
    }

    public static void setMaxMsgSize(int maxMsgSize) {
        Log.maxMsgSize = maxMsgSize;
    }

    //	public UUID getId() {
    //		return id;
    //	}

}
