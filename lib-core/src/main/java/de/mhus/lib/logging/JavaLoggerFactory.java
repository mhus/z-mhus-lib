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

import java.util.logging.Level;
import java.util.logging.Logger;

import de.mhus.lib.core.logging.LogEngine;
import de.mhus.lib.core.logging.LogFactory;
import de.mhus.lib.core.node.INode;

public class JavaLoggerFactory extends LogFactory {

    @Override
    public void init(INode config) throws Exception {}

    @Override
    public LogEngine createInstance(String name) {
        return new JLLog(Logger.getLogger(name), name);
    }

    private class JLLog extends LogEngine {
        private Logger logger;

        /** For use with a log4j factory. */
        private JLLog(Logger logger, String name) {
            super(name);
            this.logger = logger;
        }

        // ------------

        /**
         * Log a message to the Log4j Logger with <code>TRACE</code> priority. Currently logs to
         * <code>DEBUG</code> level in Log4J.
         */
        @Override
        public void trace(Object message) {
            getLogger().log(Level.ALL, String.valueOf(message));
        }

        /**
         * Log an error to the Log4j Logger with <code>TRACE</code> priority. Currently logs to
         * <code>DEBUG</code> level in Log4J.
         */
        @Override
        public void trace(Object message, Throwable t) {
            getLogger().log(Level.ALL, String.valueOf(message), t);
        }

        /** Log a message to the Log4j Logger with <code>DEBUG</code> priority. */
        @Override
        public void debug(Object message) {
            getLogger().log(Level.FINE, String.valueOf(message));
        }

        /** Log an error to the Log4j Logger with <code>DEBUG</code> priority. */
        @Override
        public void debug(Object message, Throwable t) {
            getLogger().log(Level.FINE, String.valueOf(message), t);
        }

        /** Log a message to the Log4j Logger with <code>INFO</code> priority. */
        @Override
        public void info(Object message) {
            getLogger().log(Level.INFO, String.valueOf(message));
        }

        /** Log an error to the Log4j Logger with <code>INFO</code> priority. */
        @Override
        public void info(Object message, Throwable t) {
            if (t == null) getLogger().info(String.valueOf(message));
            else getLogger().log(Level.INFO, String.valueOf(message), t);
        }

        /** Log a message to the Log4j Logger with <code>WARN</code> priority. */
        @Override
        public void warn(Object message) {
            getLogger().log(Level.WARNING, String.valueOf(message));
        }

        /** Log an error to the Log4j Logger with <code>WARN</code> priority. */
        @Override
        public void warn(Object message, Throwable t) {
            getLogger().log(Level.WARNING, String.valueOf(message), t);
        }

        /** Log a message to the Log4j Logger with <code>ERROR</code> priority. */
        @Override
        public void error(Object message) {
            getLogger().log(Level.SEVERE, String.valueOf(message));
        }

        /** Log an error to the Log4j Logger with <code>ERROR</code> priority. */
        @Override
        public void error(Object message, Throwable t) {
            getLogger().log(Level.SEVERE, String.valueOf(message), t);
        }

        /** Log a message to the Log4j Logger with <code>FATAL</code> priority. */
        @Override
        public void fatal(Object message) {
            getLogger().log(Level.SEVERE, String.valueOf(message));
        }

        /** Log an error to the Log4j Logger with <code>FATAL</code> priority. */
        @Override
        public void fatal(Object message, Throwable t) {
            getLogger().log(Level.SEVERE, String.valueOf(message), t);
        }

        /**
         * Return the native Logger instance we are using.
         *
         * @return the logger
         */
        public Logger getLogger() {
            if (logger == null) {
                logger = Logger.getLogger(getName());
            }
            return logger;
        }

        /** Check whether the Log4j Logger used is enabled for <code>DEBUG</code> priority. */
        @Override
        public boolean isDebugEnabled() {
            return getLogger().isLoggable(Level.FINE);
        }

        /** Check whether the Log4j Logger used is enabled for <code>ERROR</code> priority. */
        @Override
        public boolean isErrorEnabled() {
            return getLogger().isLoggable(Level.SEVERE);
        }

        /** Check whether the Log4j Logger used is enabled for <code>FATAL</code> priority. */
        @Override
        public boolean isFatalEnabled() {
            return getLogger().isLoggable(Level.SEVERE);
        }

        /** Check whether the Log4j Logger used is enabled for <code>INFO</code> priority. */
        @Override
        public boolean isInfoEnabled() {
            return getLogger().isLoggable(Level.INFO);
        }

        /**
         * Check whether the Log4j Logger used is enabled for <code>TRACE</code> priority. For
         * Log4J, this returns the value of <code>isDebugEnabled()</code>
         */
        @Override
        public boolean isTraceEnabled() {
            return getLogger().isLoggable(Level.FINEST);
        }

        /** Check whether the Log4j Logger used is enabled for <code>WARN</code> priority. */
        @Override
        public boolean isWarnEnabled() {
            return getLogger().isLoggable(Level.WARNING);
        }

        @Override
        public void doInitialize(LogFactory logFactory) {}

        @Override
        public void close() {}
    }
}
