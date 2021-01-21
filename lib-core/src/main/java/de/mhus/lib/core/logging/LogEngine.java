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

public abstract class LogEngine {

    private String name;

    public LogEngine(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Is debug logging currently enabled?
     *
     * <p>Call this method to prevent having to perform expensive operations (for example, <code>
     * String</code> concatenation) when the log level is more than debug.
     *
     * @return true if log level is debug
     */
    public abstract boolean isDebugEnabled();

    /**
     * Is error logging currently enabled?
     *
     * <p>Call this method to prevent having to perform expensive operations (for example, <code>
     * String</code> concatenation) when the log level is more than error.
     *
     * @return true if log level is error
     */
    public abstract boolean isErrorEnabled();

    /**
     * Is fatal logging currently enabled?
     *
     * <p>Call this method to prevent having to perform expensive operations (for example, <code>
     * String</code> concatenation) when the log level is more than fatal.
     *
     * @return true if log level is fatal
     */
    public abstract boolean isFatalEnabled();

    /**
     * Is info logging currently enabled?
     *
     * <p>Call this method to prevent having to perform expensive operations (for example, <code>
     * String</code> concatenation) when the log level is more than info.
     *
     * @return true if log level is info
     */
    public abstract boolean isInfoEnabled();

    /**
     * Is trace logging currently enabled?
     *
     * <p>Call this method to prevent having to perform expensive operations (for example, <code>
     * String</code> concatenation) when the log level is more than trace.
     *
     * @return true if log level is trace
     */
    public abstract boolean isTraceEnabled();

    /**
     * Is warn logging currently enabled?
     *
     * <p>Call this method to prevent having to perform expensive operations (for example, <code>
     * String</code> concatenation) when the log level is more than warn.
     *
     * @return true if log level is warn
     */
    public abstract boolean isWarnEnabled();

    /**
     * Log a message with trace log level.
     *
     * @param message log this message
     */
    public abstract void trace(Object message);

    /**
     * Log an error with trace log level.
     *
     * @param message log this message
     * @param t log this cause
     */
    public abstract void trace(Object message, Throwable t);

    /**
     * Log a message with debug log level.
     *
     * @param message log this message
     */
    public abstract void debug(Object message);

    /**
     * Log an error with debug log level.
     *
     * @param message log this message
     * @param t log this cause
     */
    public abstract void debug(Object message, Throwable t);

    /**
     * Log a message with info log level.
     *
     * @param message log this message
     */
    public abstract void info(Object message);

    /**
     * Log an error with info log level.
     *
     * @param message log this message
     * @param t log this cause
     */
    public abstract void info(Object message, Throwable t);

    /**
     * Log a message with warn log level.
     *
     * @param message log this message
     */
    public abstract void warn(Object message);

    /**
     * Log an error with warn log level.
     *
     * @param message log this message
     * @param t log this cause
     */
    public abstract void warn(Object message, Throwable t);

    /**
     * Log a message with error log level.
     *
     * @param message log this message
     */
    public abstract void error(Object message);

    /**
     * Log an error with error log level.
     *
     * @param message log this message
     * @param t log this cause
     */
    public abstract void error(Object message, Throwable t);

    /**
     * Log a message with fatal log level.
     *
     * @param message log this message
     */
    public abstract void fatal(Object message);

    /**
     * Log an error with fatal log level.
     *
     * @param message log this message
     * @param t log this cause
     */
    public abstract void fatal(Object message, Throwable t);

    public abstract void doInitialize(LogFactory logFactory);

    public abstract void close();
}
