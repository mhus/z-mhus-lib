/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.logging;

import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.console.Console;
import de.mhus.lib.core.console.Console.COLOR;

public class ConsoleFactory extends LogFactory {

    public static int FIX_NAME_LENGTH = 30;
    // public static boolean tracing = true;
    private Console out;
    private boolean traces = true;
    private boolean printTime = true;

    private static COLOR COLOR_TIME = COLOR.BRIGHT_BLACK;
    
    private static COLOR COLOR_TRACE = COLOR.BRIGHT_BLACK;
    private static COLOR COLOR_DEBUG = COLOR.BLUE;
    private static COLOR COLOR_INFO = COLOR.GREEN;
    private static COLOR COLOR_WARN = COLOR.BRIGHT_RED;
    private static COLOR COLOR_ERROR = COLOR.RED;
    private static COLOR COLOR_FATAL = COLOR.MAGENTA;

    private static COLOR COLOR_NAME = COLOR.BRIGHT_BLACK;
    private static COLOR COLOR_MESSAGE = COLOR.BRIGHT_WHITE;

    @SuppressWarnings("unused")
    private IConfig config;

    public ConsoleFactory() {
        out = Console.get();
    }

    @Override
    public LogEngine createInstance(String name) {
        return new ConsoleLog(name);
    }

    public ConsoleFactory(IConfig config) throws Exception {
        //		name = config.getExtracted("name","");
        init(config);
    }

    public String printTime() {
        if (printTime) {
            return MDate.toIso8601(System.currentTimeMillis()) + " ";
        }
        return "";
    }

    @Override
    public void init(IConfig config) throws Exception {
        if (config == null) return;

        this.config = config;
        printTime = config.getBoolean("TIME", printTime);
        String newLevel = config.getExtracted("LEVEL", level.name());
        if (newLevel != null) level = Log.LEVEL.valueOf(newLevel.toUpperCase());

        traces = config.getBoolean("traces", true);
        
        try {
            String col = config.getString("COLOR_TIME", null);
            if (col != null)
                COLOR_TIME = COLOR.valueOf(col.toUpperCase());
        } catch (Throwable t) {}
        try {
            String col = config.getString("COLOR_TRACE", null);
            if (col != null)
                COLOR_TRACE = COLOR.valueOf(col.toUpperCase());
        } catch (Throwable t) {}
        try {
            String col = config.getString("COLOR_DEBUG", null);
            if (col != null)
                COLOR_DEBUG = COLOR.valueOf(col.toUpperCase());
        } catch (Throwable t) {}
        try {
            String col = config.getString("COLOR_INFO", null);
            if (col != null)
                COLOR_INFO = COLOR.valueOf(col.toUpperCase());
        } catch (Throwable t) {}
        try {
            String col = config.getString("COLOR_WARN", null);
            if (col != null)
                COLOR_WARN = COLOR.valueOf(col.toUpperCase());
        } catch (Throwable t) {}
        try {
            String col = config.getString("COLOR_ERROR", null);
            if (col != null)
                COLOR_ERROR = COLOR.valueOf(col.toUpperCase());
        } catch (Throwable t) {}
        try {
            String col = config.getString("COLOR_FATAL", null);
            if (col != null)
                COLOR_FATAL = COLOR.valueOf(col.toUpperCase());
        } catch (Throwable t) {}
        try {
            String col = config.getString("COLOR_NAME", null);
            if (col != null)
                COLOR_NAME = COLOR.valueOf(col.toUpperCase());
        } catch (Throwable t) {}
        try {
            String col = config.getString("COLOR_MESSAGE", null);
            if (col != null)
                COLOR_MESSAGE = COLOR.valueOf(col.toUpperCase());
        } catch (Throwable t) {}
        
        FIX_NAME_LENGTH = config.getInt("FIX_NAME_LENGTH", FIX_NAME_LENGTH);
        
    }

    public Log.LEVEL getLevel() {
        return level;
    }

    public void setLevel(Log.LEVEL level) {
        this.level = level;
    }

    private class ConsoleLog extends LogEngine {

        private String fixName;

        public ConsoleLog(String name) {
            super(name);
        }

        @Override
        public void debug(Object message) {
            if (!isDebugEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_DEBUG, null);
            out.print("DEBUG ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (message != null && message instanceof Throwable)
                ((Throwable) message).printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void debug(Object message, Throwable t) {
            if (!isDebugEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_DEBUG, null);
            out.print("DEBUG ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (t != null && traces) t.printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void error(Object message) {
            if (!isErrorEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_ERROR, null);
            out.print("ERROR ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (message != null && message instanceof Throwable && traces)
                ((Throwable) message).printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void error(Object message, Throwable t) {
            if (!isErrorEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_ERROR, null);
            out.print("ERROR ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (t != null && traces) t.printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void fatal(Object message) {
            if (!isFatalEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_FATAL, null);
            out.print("FATAL ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (message != null && message instanceof Throwable && traces)
                ((Throwable) message).printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void fatal(Object message, Throwable t) {
            if (!isFatalEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_FATAL, null);
            out.print("FATAL ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (t != null && traces) t.printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void info(Object message) {
            if (!isInfoEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_INFO, null);
            out.print("INFO  ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (message != null && message instanceof Throwable && traces)
                ((Throwable) message).printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void info(Object message, Throwable t) {
            if (!isInfoEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_INFO, null);
            out.print("INFO  ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (t != null && traces) t.printStackTrace(out);
            out.cleanup();
        }

        @Override
        public boolean isDebugEnabled() {
            return getLevel().ordinal() <= Log.LEVEL.DEBUG.ordinal();
        }

        @Override
        public boolean isErrorEnabled() {
            return getLevel().ordinal() <= Log.LEVEL.ERROR.ordinal();
        }

        @Override
        public boolean isFatalEnabled() {
            return getLevel().ordinal() <= Log.LEVEL.FATAL.ordinal();
        }

        @Override
        public boolean isInfoEnabled() {
            return getLevel().ordinal() <= Log.LEVEL.INFO.ordinal();
        }

        @Override
        public boolean isTraceEnabled() {
            return getLevel().ordinal() <= Log.LEVEL.TRACE.ordinal();
        }

        @Override
        public boolean isWarnEnabled() {
            return getLevel().ordinal() <= Log.LEVEL.WARN.ordinal();
        }

        @Override
        public void trace(Object message) {
            if (!isTraceEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_TRACE, null);
            out.print("TRACE ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (message != null && message instanceof Throwable && traces)
                ((Throwable) message).printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void trace(Object message, Throwable t) {
            if (!isTraceEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_TRACE, null);
            out.print("TRACE ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (t != null && traces) t.printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void warn(Object message) {
            if (!isWarnEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_WARN, null);
            out.print("WARN  ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (message != null && message instanceof Throwable && traces)
                ((Throwable) message).printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void warn(Object message, Throwable t) {
            if (!isWarnEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_WARN, null);
            out.print("WARN  ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (t != null && traces) t.printStackTrace(out);
            out.cleanup();
        }

        private String getFixName() {
            if (fixName == null) {
                String n = getName();
                if (n.length() > FIX_NAME_LENGTH)
                    n = n.substring(0, FIX_NAME_LENGTH);
                else if (n.length() < FIX_NAME_LENGTH)
                n = n + MString.rep(' ', FIX_NAME_LENGTH - n.length());
                fixName = n;
            }
            return fixName;
        }

        @Override
        public void doInitialize(LogFactory logFactory) {}

        @Override
        public void close() {}
    }
}
