/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.logging.LogEngine;
import de.mhus.lib.core.logging.LogFactory;

public class SLF4JFactory extends LogFactory {

    @Override
    public LogEngine createInstance(String name) {
        return new SLF4JLog(LoggerFactory.getLogger(name));
    }

    @Override
    public void init(IConfig config) throws Exception {}

    private class SLF4JLog extends LogEngine {

        private Logger logger;

        private SLF4JLog(Logger logger) {
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
            getLogger().trace(String.valueOf(message));
        }

        /**
         * Log an error to the Log4j Logger with <code>TRACE</code> priority. Currently logs to
         * <code>DEBUG</code> level in Log4J.
         */
        @Override
        public void trace(Object message, Throwable t) {
            getLogger().trace(String.valueOf(message), t);
        }

        /** Log a message to the Log4j Logger with <code>DEBUG</code> priority. */
        @Override
        public void debug(Object message) {
            getLogger().debug(String.valueOf(message));
        }

        /** Log an error to the Log4j Logger with <code>DEBUG</code> priority. */
        @Override
        public void debug(Object message, Throwable t) {
            getLogger().debug(String.valueOf(message), t);
        }

        /** Log a message to the Log4j Logger with <code>INFO</code> priority. */
        @Override
        public void info(Object message) {
            getLogger().info(String.valueOf(message));
        }

        /** Log an error to the Log4j Logger with <code>INFO</code> priority. */
        @Override
        public void info(Object message, Throwable t) {
            getLogger().info(String.valueOf(message), t);
        }

        /** Log a message to the Log4j Logger with <code>WARN</code> priority. */
        @Override
        public void warn(Object message) {
            getLogger().warn(String.valueOf(message));
        }

        /** Log an error to the Log4j Logger with <code>WARN</code> priority. */
        @Override
        public void warn(Object message, Throwable t) {
            getLogger().warn(String.valueOf(message), t);
        }

        /** Log a message to the Log4j Logger with <code>ERROR</code> priority. */
        @Override
        public void error(Object message) {
            getLogger().error(String.valueOf(message));
        }

        /** Log an error to the Log4j Logger with <code>ERROR</code> priority. */
        @Override
        public void error(Object message, Throwable t) {
            getLogger().error(String.valueOf(message), t);
        }

        /** Log a message to the Log4j Logger with <code>FATAL</code> priority. */
        @Override
        public void fatal(Object message) {
            getLogger().error(String.valueOf(message));
        }

        /** Log an error to the Log4j Logger with <code>FATAL</code> priority. */
        @Override
        public void fatal(Object message, Throwable t) {
            getLogger().error(String.valueOf(message), t);
        }

        /**
         * Return the native Logger instance we are using.
         *
         * @return the logger
         */
        public Logger getLogger() {
            if (logger == null) {
                logger = LoggerFactory.getLogger(getName());
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
            return getLogger().isErrorEnabled();
        }

        /** Check whether the Log4j Logger used is enabled for <code>FATAL</code> priority. */
        @Override
        public boolean isFatalEnabled() {
            return logger.isErrorEnabled();
        }

        /** Check whether the Log4j Logger used is enabled for <code>INFO</code> priority. */
        @Override
        public boolean isInfoEnabled() {
            return getLogger().isInfoEnabled();
        }

        /**
         * Check whether the Log4j Logger used is enabled for <code>TRACE</code> priority. For
         * Log4J, this returns the value of <code>isDebugEnabled()</code>
         */
        @Override
        public boolean isTraceEnabled() {
            return getLogger().isTraceEnabled();
        }

        /** Check whether the Log4j Logger used is enabled for <code>WARN</code> priority. */
        @Override
        public boolean isWarnEnabled() {
            return getLogger().isWarnEnabled();
        }

        @Override
        public void doInitialize(LogFactory logFactory) {}

        @Override
        public void close() {}
    }
}
