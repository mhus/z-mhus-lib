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
package de.mhus.lib.logging;

import java.net.URI;

import org.apache.commons.logging.LogConfigurationException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import de.mhus.lib.core.logging.LogEngine;
import de.mhus.lib.core.logging.LogFactory;
import de.mhus.lib.core.node.INode;

/** Concrete subclass of {@link LogFactory} specific to log4j. */
public final class Log4JFactory extends LogFactory {

    @Override
    public void init(INode config) throws Exception {
        if (config == null) return;

        // ResourceNode<?> ccc = config.getNode("configuration");
        String configFile = config.getExtracted("configuration");

        if (configFile != null) {
            LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            ctx.setConfigLocation(URI.create(configFile));
        }
        //        if (ccc != null && ccc instanceof XmlConfig) {
        //            // MLog.t("configure inline");
        //            org.apache.logging.log4j.core.config.Configurator.
        //            DOMConfigurator.configure(((XmlConfig) ccc).getXmlElement());
        //        } else if (configFile == null) {
        //            // NOOP
        //        } else if (configFile.endsWith(".properties")) {
        //            // MLog.t("configure properties",configFile);
        //            PropertyConfigurator.configureAndWatch(configFile);
        //        } else if (configFile.endsWith(".xml")) {
        //            // MLog.t("configure xml",configFile);
        //            DOMConfigurator.configureAndWatch(configFile);
        //        }
    }

    public Log4JFactory() {
        super();
    }

    /**
     * Convenience method to derive a name from the specified class and call <code>
     * getInstance(String)</code> with it.
     *
     * @param name Class for which a suitable Log name will be derived
     * @exception LogConfigurationException if a suitable <code>Log</code> instance cannot be
     *     returned
     */
    @Override
    public LogEngine createInstance(String name) {
        Log4JLog instance = new Log4JLog(LogManager.getLogger(name));
        return instance;
    }

    public class Log4JLog extends LogEngine {

        //   private String name;
        private Logger logger;

        /** For use with a log4j factory. */
        private Log4JLog(Logger logger) {
            super(logger.getName());
            this.logger = logger;
        }

        // ------------

        /**
         * Log a message to the Log4j Logger with <code>TRACE</code> priority. Currently logs to
         * <code>DEBUG</code> level in Log4J.
         */
        @Override
        public void trace(Object message) {
            getLogger().log(Level.TRACE, message, null);
        }

        /**
         * Log an error to the Log4j Logger with <code>TRACE</code> priority. Currently logs to
         * <code>DEBUG</code> level in Log4J.
         */
        @Override
        public void trace(Object message, Throwable t) {
            getLogger().log(Level.TRACE, message, t);
        }

        /** Log a message to the Log4j Logger with <code>DEBUG</code> priority. */
        @Override
        public void debug(Object message) {
            getLogger().log(Level.DEBUG, message, null);
        }

        /** Log an error to the Log4j Logger with <code>DEBUG</code> priority. */
        @Override
        public void debug(Object message, Throwable t) {
            getLogger().log(Level.DEBUG, message, t);
        }

        /** Log a message to the Log4j Logger with <code>INFO</code> priority. */
        @Override
        public void info(Object message) {
            getLogger().log(Level.INFO, message, null);
        }

        /** Log an error to the Log4j Logger with <code>INFO</code> priority. */
        @Override
        public void info(Object message, Throwable t) {
            getLogger().log(Level.INFO, message, t);
        }

        /** Log a message to the Log4j Logger with <code>WARN</code> priority. */
        @Override
        public void warn(Object message) {
            getLogger().log(Level.WARN, message, null);
        }

        /** Log an error to the Log4j Logger with <code>WARN</code> priority. */
        @Override
        public void warn(Object message, Throwable t) {
            getLogger().log(Level.WARN, message, t);
        }

        /** Log a message to the Log4j Logger with <code>ERROR</code> priority. */
        @Override
        public void error(Object message) {
            getLogger().log(Level.ERROR, message, null);
        }

        /** Log an error to the Log4j Logger with <code>ERROR</code> priority. */
        @Override
        public void error(Object message, Throwable t) {
            getLogger().log(Level.ERROR, message, t);
        }

        /** Log a message to the Log4j Logger with <code>FATAL</code> priority. */
        @Override
        public void fatal(Object message) {
            getLogger().log(Level.FATAL, message, null);
        }

        /** Log an error to the Log4j Logger with <code>FATAL</code> priority. */
        @Override
        public void fatal(Object message, Throwable t) {
            getLogger().log(Level.FATAL, message, t);
        }

        /**
         * Return the native Logger instance we are using.
         *
         * @return the logger
         */
        public Logger getLogger() {
            if (logger == null) {
                logger = LogManager.getLogger(getName());
            }
            return (this.logger);
        }

        /** Check whether the Log4j Logger used is enabled for <code>DEBUG</code> priority. */
        @Override
        public boolean isDebugEnabled() {
            return getLogger().isDebugEnabled();
        }

        /** Check whether the Log4j Logger used is enabled for <code>ERROR</code> priority. */
        @Override
        public boolean isErrorEnabled() {
            return getLogger().isEnabled(Level.ERROR);
        }

        /** Check whether the Log4j Logger used is enabled for <code>FATAL</code> priority. */
        @Override
        public boolean isFatalEnabled() {
            return getLogger().isEnabled(Level.FATAL);
        }

        /** Check whether the Log4j Logger used is enabled for <code>INFO</code> priority. */
        @Override
        public boolean isInfoEnabled() {
            return getLogger().isEnabled(Level.INFO);
        }

        /**
         * Check whether the Log4j Logger used is enabled for <code>TRACE</code> priority. For
         * Log4J, this returns the value of <code>isDebugEnabled()</code>
         */
        @Override
        public boolean isTraceEnabled() {
            return getLogger().isDebugEnabled();
        }

        /** Check whether the Log4j Logger used is enabled for <code>WARN</code> priority. */
        @Override
        public boolean isWarnEnabled() {
            return getLogger().isEnabled(Level.WARN);
        }

        @Override
        public void doInitialize(LogFactory logFactory) {}

        @Override
        public void close() {}
    }
}
