package de.mhus.lib.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.logging.LogEngine;
import de.mhus.lib.core.logging.LogFactory;

/**
 * <p>JavaLoggerFactory class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class JavaLoggerFactory extends LogFactory {

	/** {@inheritDoc} */
	@Override
	public void init(ResourceNode config) throws Exception {
		
	}

	/** {@inheritDoc} */
	@Override
	public LogEngine createInstance(String name) {
		return new JLLog(Logger.getLogger(name), name);
	}

	private class JLLog extends LogEngine {
		private Logger logger;
		
	    /** For use with a log4j factory.
	     */
	    private JLLog(Logger logger, String name ) {
	        super(name);
	        this.logger=logger;
	    }
	    
	    // ------------
	    
	
	    /**
	     * Log a message to the Log4j Logger with <code>TRACE</code> priority.
	     * Currently logs to <code>DEBUG</code> level in Log4J.
	     */
	    @Override
		public void trace(Object message) {
            getLogger().log(Level.ALL, String.valueOf(message) );
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>TRACE</code> priority.
	     * Currently logs to <code>DEBUG</code> level in Log4J.
	     */
	    @Override
		public void trace(Object message, Throwable t) {
            getLogger().log(Level.ALL, String.valueOf(message), t );
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>DEBUG</code> priority.
	     */
	    @Override
		public void debug(Object message) {
            getLogger().log(Level.FINE, String.valueOf(message) );
	    }
	
	    /**
	     * Log an error to the Log4j Logger with <code>DEBUG</code> priority.
	     */
	    @Override
		public void debug(Object message, Throwable t) {
            getLogger().log(Level.FINE, String.valueOf(message), t );
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>INFO</code> priority.
	     */
	    @Override
		public void info(Object message) {
            getLogger().log(Level.INFO, String.valueOf(message) );
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>INFO</code> priority.
	     */
	    @Override
		public void info(Object message, Throwable t) {
	    	if (t == null)
	    		getLogger().info(String.valueOf(message));
	    	else
	    		getLogger().log(Level.INFO, String.valueOf(message), t );
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>WARN</code> priority.
	     */
	    @Override
		public void warn(Object message) {
            getLogger().log(Level.WARNING, String.valueOf(message) );
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>WARN</code> priority.
	     */
	    @Override
		public void warn(Object message, Throwable t) {
            getLogger().log(Level.WARNING, String.valueOf(message), t );
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>ERROR</code> priority.
	     */
	    @Override
		public void error(Object message) {
            getLogger().log(Level.SEVERE, String.valueOf(message) );
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>ERROR</code> priority.
	     */
	    @Override
		public void error(Object message, Throwable t) {
            getLogger().log(Level.SEVERE, String.valueOf(message), t );
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>FATAL</code> priority.
	     */
	    @Override
		public void fatal(Object message) {
            getLogger().log(Level.SEVERE, String.valueOf(message) );
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>FATAL</code> priority.
	     */
	    @Override
		public void fatal(Object message, Throwable t) {
            getLogger().log(Level.SEVERE, String.valueOf(message), t );
	    }
	
	
	    /**
	     * Return the native Logger instance we are using.
	     * @return x
	     */
	    public Logger getLogger() {
	        if (logger == null) {
	            logger = Logger.getLogger(getName());
	        }
	        return logger;
	    }
	
	
	    /**
	     * Check whether the Log4j Logger used is enabled for <code>DEBUG</code> priority.
	     */
	    @Override
		public boolean isDebugEnabled() {
	        return getLogger().isLoggable(Level.FINE);
	    }
	
	
	     /**
	     * Check whether the Log4j Logger used is enabled for <code>ERROR</code> priority.
	     */
	    @Override
		public boolean isErrorEnabled() {
	        return getLogger().isLoggable(Level.SEVERE);
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
	        return getLogger().isLoggable(Level.INFO);
	    }
	
	
	    /**
	     * Check whether the Log4j Logger used is enabled for <code>TRACE</code> priority.
	     * For Log4J, this returns the value of <code>isDebugEnabled()</code>
	     */
	    @Override
		public boolean isTraceEnabled() {
	        return getLogger().isLoggable(Level.FINEST);
	    }
	
	    /**
	     * Check whether the Log4j Logger used is enabled for <code>WARN</code> priority.
	     */
	    @Override
		public boolean isWarnEnabled() {
	        return getLogger().isLoggable(Level.WARNING);
	    }

		@Override
		public void doInitialize(LogFactory logFactory) {
			
		}

		@Override
		public void close() {
		}
	}
}
