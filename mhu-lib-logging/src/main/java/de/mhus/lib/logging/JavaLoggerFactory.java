package de.mhus.lib.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.LogFactory;

public class JavaLoggerFactory extends LogFactory {

	@Override
	public void init(ResourceNode config) throws Exception {
		
	}

	@Override
	public Log createInstance(String name) {
		return new JLLog(Logger.getLogger(name));
	}

	private class JLLog extends Log {
		private Logger logger;
		
	    /** For use with a log4j factory.
	     */
	    private JLLog(Logger logger ) {
	        super(logger.getName());
	        this.logger=logger;
	    }
	    
	    // ------------
	    
	
	    /**
	     * Log a message to the Log4j Logger with <code>TRACE</code> priority.
	     * Currently logs to <code>DEBUG</code> level in Log4J.
	     */
	    @Override
		public void trace(Object message) {
	    	if (!isTrace()) return;
            getLogger().log(Level.ALL, String.valueOf(message) );
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>TRACE</code> priority.
	     * Currently logs to <code>DEBUG</code> level in Log4J.
	     */
	    @Override
		public void trace(Object message, Throwable t) {
	    	if (!isTrace()) return;
            getLogger().log(Level.ALL, String.valueOf(message), t );
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>DEBUG</code> priority.
	     */
	    @Override
		public void debug(Object message) {
            getLogger().log(isTrace() ? Level.ALL : Level.FINE, String.valueOf(message) );
	    }
	
	    /**
	     * Log an error to the Log4j Logger with <code>DEBUG</code> priority.
	     */
	    @Override
		public void debug(Object message, Throwable t) {
            getLogger().log(isTrace() ? Level.ALL : Level.FINE, String.valueOf(message), t );
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>INFO</code> priority.
	     */
	    @Override
		public void info(Object message) {
            getLogger().log(isTrace() ? Level.ALL : Level.INFO, String.valueOf(message) );
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>INFO</code> priority.
	     */
	    @Override
		public void info(Object message, Throwable t) {
            getLogger().log(isTrace() ? Level.ALL : Level.INFO, String.valueOf(message), t );
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>WARN</code> priority.
	     */
	    @Override
		public void warn(Object message) {
            getLogger().log(isTrace() ? Level.ALL : Level.WARNING, String.valueOf(message) );
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>WARN</code> priority.
	     */
	    @Override
		public void warn(Object message, Throwable t) {
            getLogger().log(isTrace() ? Level.ALL : Level.WARNING, String.valueOf(message), t );
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>ERROR</code> priority.
	     */
	    @Override
		public void error(Object message) {
            getLogger().log(isTrace() ? Level.ALL : Level.SEVERE, String.valueOf(message) );
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>ERROR</code> priority.
	     */
	    @Override
		public void error(Object message, Throwable t) {
            getLogger().log(isTrace() ? Level.ALL : Level.SEVERE, String.valueOf(message), t );
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>FATAL</code> priority.
	     */
	    @Override
		public void fatal(Object message) {
            getLogger().log(isTrace() ? Level.ALL : Level.SEVERE, String.valueOf(message) );
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>FATAL</code> priority.
	     */
	    @Override
		public void fatal(Object message, Throwable t) {
            getLogger().log(isTrace() ? Level.ALL : Level.SEVERE, String.valueOf(message), t );
	    }
	
	
	    /**
	     * Return the native Logger instance we are using.
	     * @return 
	     */
	    public Logger getLogger() {
	        if (logger == null) {
	            logger = Logger.getLogger(getName());
	        }
	        return (this.logger);
	    }
	
	
	    /**
	     * Check whether the Log4j Logger used is enabled for <code>DEBUG</code> priority.
	     */
	    @Override
		public boolean isDebugEnabled() {
	        return isTrace() || getLogger().isLoggable(Level.FINE);
	    }
	
	
	     /**
	     * Check whether the Log4j Logger used is enabled for <code>ERROR</code> priority.
	     */
	    @Override
		public boolean isErrorEnabled() {
	        return isTrace() || getLogger().isLoggable(Level.SEVERE);
	    }
	
	
	    /**
	     * Check whether the Log4j Logger used is enabled for <code>FATAL</code> priority.
	     */
	    @Override
		public boolean isFatalEnabled() {
	        return getLogger().isLoggable(Level.SEVERE);
	    }
	
	
	    /**
	     * Check whether the Log4j Logger used is enabled for <code>INFO</code> priority.
	     */
	    @Override
		public boolean isInfoEnabled() {
	        return isTrace() || getLogger().isLoggable(Level.INFO);
	    }
	
	
	    /**
	     * Check whether the Log4j Logger used is enabled for <code>TRACE</code> priority.
	     * For Log4J, this returns the value of <code>isDebugEnabled()</code>
	     */
	    @Override
		public boolean isTraceEnabled() {
	        return isTrace() || getLogger().isLoggable(Level.FINEST);
	    }
	
	    /**
	     * Check whether the Log4j Logger used is enabled for <code>WARN</code> priority.
	     */
	    @Override
		public boolean isWarnEnabled() {
	        return isTrace() || getLogger().isLoggable(Level.WARNING);
	    }
	}
}
