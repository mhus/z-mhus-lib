package de.mhus.lib.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.LogEngine;
import de.mhus.lib.core.logging.LogFactory;

public class SLF4JFactory extends LogFactory {

	@Override
	public LogEngine createInstance(String name) {
		return new SLF4JLog(LoggerFactory.getLogger(name));
	}


	@Override
	public void init(ResourceNode config) throws Exception {
		
	}
	private class SLF4JLog extends LogEngine {

		private Logger logger;
		
	    private SLF4JLog(Logger logger ) {
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
	    	getLogger().trace(String.valueOf(message));
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>TRACE</code> priority.
	     * Currently logs to <code>DEBUG</code> level in Log4J.
	     */
	    @Override
		public void trace(Object message, Throwable t) {
            getLogger().trace(String.valueOf(message),t);
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>DEBUG</code> priority.
	     */
	    @Override
		public void debug(Object message) {
            getLogger().debug(String.valueOf(message));
	    }
	
	    /**
	     * Log an error to the Log4j Logger with <code>DEBUG</code> priority.
	     */
	    @Override
		public void debug(Object message, Throwable t) {
            getLogger().debug(String.valueOf(message),t);
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>INFO</code> priority.
	     */
	    @Override
		public void info(Object message) {
    		getLogger().info(String.valueOf(message));
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>INFO</code> priority.
	     */
	    @Override
		public void info(Object message, Throwable t) {
    		getLogger().info(String.valueOf(message),t);
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>WARN</code> priority.
	     */
	    @Override
		public void warn(Object message) {
    		getLogger().warn(String.valueOf(message));
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>WARN</code> priority.
	     */
	    @Override
		public void warn(Object message, Throwable t) {
    		getLogger().warn(String.valueOf(message),t);
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>ERROR</code> priority.
	     */
	    @Override
		public void error(Object message) {
	    	getLogger().error(String.valueOf(message));
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>ERROR</code> priority.
	     */
	    @Override
		public void error(Object message, Throwable t) {
	    	getLogger().error(String.valueOf(message),t);
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>FATAL</code> priority.
	     */
	    @Override
		public void fatal(Object message) {
            getLogger().error(String.valueOf(message));
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>FATAL</code> priority.
	     */
	    @Override
		public void fatal(Object message, Throwable t) {
            getLogger().error(String.valueOf(message),t);
	    }
	
	
	    /**
	     * Return the native Logger instance we are using.
	     * @return 
	     */
	    public Logger getLogger() {
	        if (logger == null) {
	            logger = LoggerFactory.getLogger(getName());
	        }
	        return (this.logger);
	    }
	
	
	    /**
	     * Check whether the Log4j Logger used is enabled for <code>DEBUG</code> priority.
	     */
	    @Override
		public boolean isDebugEnabled() {
	        return getLogger().isDebugEnabled();
	    }
	
	
	     /**
	     * Check whether the Log4j Logger used is enabled for <code>ERROR</code> priority.
	     */
	    @Override
		public boolean isErrorEnabled() {
            return getLogger().isErrorEnabled();
	    }
	
	
	    /**
	     * Check whether the Log4j Logger used is enabled for <code>FATAL</code> priority.
	     */
	    @Override
		public boolean isFatalEnabled() {
	    	return logger.isErrorEnabled();
	    }
	
	
	    /**
	     * Check whether the Log4j Logger used is enabled for <code>INFO</code> priority.
	     */
	    @Override
		public boolean isInfoEnabled() {
	        return getLogger().isInfoEnabled();
	    }
	
	
	    /**
	     * Check whether the Log4j Logger used is enabled for <code>TRACE</code> priority.
	     * For Log4J, this returns the value of <code>isDebugEnabled()</code>
	     */
	    @Override
		public boolean isTraceEnabled() {
	        return getLogger().isTraceEnabled();
	    }
	
	    /**
	     * Check whether the Log4j Logger used is enabled for <code>WARN</code> priority.
	     */
	    @Override
		public boolean isWarnEnabled() {
	        return getLogger().isWarnEnabled();
	    }

		@Override
		public void doInitialize(LogFactory logFactory) {
			
		}
	}

}
