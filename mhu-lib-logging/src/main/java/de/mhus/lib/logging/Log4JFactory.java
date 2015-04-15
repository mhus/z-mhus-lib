package de.mhus.lib.logging;

/*
 * Copyright 2001-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import de.mhus.lib.core.config.XmlConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.LogFactory;

/**
 * <p>Concrete subclass of {@link LogFactory} specific to log4j.
 *
 * @author Costin Manolache
 */
public final class Log4JFactory extends LogFactory {

    private static final boolean is12 = Priority.class.isAssignableFrom(Level.class);
    private static final String FQCN = Log4JFactory.class.getName();

    @Override
	public void init(ResourceNode config) throws Exception {
    	if (config == null) return;
    		
		ResourceNode ccc = config.getNode("configuration");
		String configFile = config.getExtracted("configuration");
		
		if (ccc != null && ccc instanceof XmlConfig) {
			//MLog.t("configure inline");
			DOMConfigurator.configure( ((XmlConfig)ccc).getXmlElement() );
		} else
		if (configFile == null) {
			// NOOP
		} else
		if (configFile.endsWith(".properties")) {
			//MLog.t("configure properties",configFile);
			PropertyConfigurator.configureAndWatch(configFile);
		} else
		if (configFile.endsWith(".xml")) {
			//MLog.t("configure xml",configFile);
			DOMConfigurator.configureAndWatch(configFile);
		}
		
	
    }
    
	public Log4JFactory() {
        super();
    }

    /**
     * Convenience method to derive a name from the specified class and
     * call <code>getInstance(String)</code> with it.
     *
     * @param clazz Class for which a suitable Log name will be derived
     *
     * @exception LogConfigurationException if a suitable <code>Log</code>
     *  instance cannot be returned
     */

    @Override
	public Log createInstance(String name)
    {
    	Log4JLog instance = new Log4JLog( Logger.getLogger( name ));
        return instance;
    }

	public class Log4JLog extends Log {
	
	 //   private String name;
		private Logger logger;

	    /** For use with a log4j factory.
	     */
	    private Log4JLog(Logger logger ) {
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
	        if(is12) {
	            getLogger().log(FQCN, (Priority) Level.DEBUG, message, null );
	        } else {
	            getLogger().log(FQCN, Level.DEBUG, message, null );
	        }
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>TRACE</code> priority.
	     * Currently logs to <code>DEBUG</code> level in Log4J.
	     */
	    @Override
		public void trace(Object message, Throwable t) {
	    	if (!isTrace()) return;
	        if(is12) {
	            getLogger().log(FQCN, (Priority) Level.DEBUG, message, t );
	        } else {
	            getLogger().log(FQCN, Level.DEBUG, message, t );
	        }
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>DEBUG</code> priority.
	     */
	    @Override
		public void debug(Object message) {
	        if(is12) {
	            getLogger().log(FQCN, (Priority) Level.DEBUG, message, null );
	        } else {
	            getLogger().log(FQCN, Level.DEBUG, message, null );
	        }
	    }
	
	    /**
	     * Log an error to the Log4j Logger with <code>DEBUG</code> priority.
	     */
	    @Override
		public void debug(Object message, Throwable t) {
	        if(is12) {
	            getLogger().log(FQCN, (Priority) Level.DEBUG, message, t );
	        } else {
	            getLogger().log(FQCN, Level.DEBUG, message, t );
	        }
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>INFO</code> priority.
	     */
	    @Override
		public void info(Object message) {
	        if(is12) {
	            getLogger().log(FQCN, (Priority) Level.INFO, message, null );
	        } else {
	            getLogger().log(FQCN, Level.INFO, message, null );
	        }    
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>INFO</code> priority.
	     */
	    @Override
		public void info(Object message, Throwable t) {
	        if(is12) {
	            getLogger().log(FQCN, (Priority) Level.INFO, message, t );
	        } else {
	            getLogger().log(FQCN, Level.INFO, message, t );
	        }
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>WARN</code> priority.
	     */
	    @Override
		public void warn(Object message) {
	        if(is12) {
	            getLogger().log(FQCN, (Priority) Level.WARN, message, null );
	        } else {
	            getLogger().log(FQCN, Level.WARN, message, null );
	        }
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>WARN</code> priority.
	     */
	    @Override
		public void warn(Object message, Throwable t) {
	        if(is12) {
	            getLogger().log(FQCN, (Priority) Level.WARN, message, t );
	        } else {
	            getLogger().log(FQCN, Level.WARN, message, t );
	        }
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>ERROR</code> priority.
	     */
	    @Override
		public void error(Object message) {
	        if(is12) {
	            getLogger().log(FQCN, (Priority) Level.ERROR, message, null );
	        } else {
	            getLogger().log(FQCN, Level.ERROR, message, null );
	        }
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>ERROR</code> priority.
	     */
	    @Override
		public void error(Object message, Throwable t) {
	        if(is12) {
	            getLogger().log(FQCN, (Priority) Level.ERROR, message, t );
	        } else {
	            getLogger().log(FQCN, Level.ERROR, message, t );
	        }
	    }
	
	
	    /**
	     * Log a message to the Log4j Logger with <code>FATAL</code> priority.
	     */
	    @Override
		public void fatal(Object message) {
	        if(is12) {
	            getLogger().log(FQCN, (Priority) Level.FATAL, message, null );
	        } else {
	            getLogger().log(FQCN, Level.FATAL, message, null );
	        }
	    }
	
	
	    /**
	     * Log an error to the Log4j Logger with <code>FATAL</code> priority.
	     */
	    @Override
		public void fatal(Object message, Throwable t) {
	        if(is12) {
	            getLogger().log(FQCN, (Priority) Level.FATAL, message, t );
	        } else {
	            getLogger().log(FQCN, Level.FATAL, message, t );
	        }
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
	        return getLogger().isDebugEnabled();
	    }
	
	
	     /**
	     * Check whether the Log4j Logger used is enabled for <code>ERROR</code> priority.
	     */
	    @Override
		public boolean isErrorEnabled() {
	        if(is12) {
	            return getLogger().isEnabledFor((Priority) Level.ERROR);
	        } else {
	            return getLogger().isEnabledFor(Level.ERROR);
	        }
	    }
	
	
	    /**
	     * Check whether the Log4j Logger used is enabled for <code>FATAL</code> priority.
	     */
	    @Override
		public boolean isFatalEnabled() {
	        if(is12) {
	            return getLogger().isEnabledFor((Priority) Level.FATAL);
	        } else {
	            return getLogger().isEnabledFor(Level.FATAL);
	        }
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
	        return getLogger().isDebugEnabled();
	    }
	
	    /**
	     * Check whether the Log4j Logger used is enabled for <code>WARN</code> priority.
	     */
	    @Override
		public boolean isWarnEnabled() {
	        if(is12) {
	            return getLogger().isEnabledFor((Priority) Level.WARN);
	        } else {
	            return getLogger().isEnabledFor(Level.WARN);
	        }
	    }
	}
}
