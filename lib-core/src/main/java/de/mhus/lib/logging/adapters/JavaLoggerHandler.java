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
package de.mhus.lib.logging.adapters;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import de.mhus.lib.core.logging.Log;

public class JavaLoggerHandler extends Handler {

    public JavaLoggerHandler() {}

    @Override
    public void publish(LogRecord record) {
        Level level = record.getLevel();
        String loggerName = record.getLoggerName();
        String msg = record.getMessage();
        String srcClass = record.getSourceClassName();
        String srcMethod = record.getSourceMethodName();
        Throwable t = record.getThrown();

        Log logger = Log.getLog(loggerName);

        String method = srcClass + "." + srcMethod;

        if (level.intValue() == Level.INFO.intValue()) logger.i(method, msg, t);
        else if (level.intValue() == Level.WARNING.intValue()) logger.w(method, msg, t);
        else if (level.intValue() == Level.SEVERE.intValue()) logger.e(method, msg, t);
        else if (level.intValue() == Level.FINE.intValue()) logger.d(method, msg, t);
        else if (level.intValue() == Level.FINER.intValue()) logger.t(method, msg, t);
        else logger.t(method, msg, t);
    }

    @Override
    public void flush() {}

    @Override
    public void close() throws SecurityException {}

    public static void configure() {

        String config =
                "handlers = "
                        + JavaLoggerHandler.class.getCanonicalName()
                        + "\n"
                        + ".level = ALL"
                        + "\n"
                        + JavaLoggerHandler.class.getCanonicalName()
                        + ".level = ALL";
        InputStream ins = new ByteArrayInputStream(config.getBytes());
        try {
            LogManager.getLogManager().readConfiguration(ins);
        } catch (Throwable t) {
            Log.getLog(JavaLoggerHandler.class).e("configure", t);
        }

        //		Handler handler = new JavaLoggerHandler();
        //		handler.setLevel(Level.ALL);
        //		Logger root = LogManager.getLogManager().getLogger("");
        //		if (root != null) {
        //			root.addHandler(handler);
        //		}
    }
}
