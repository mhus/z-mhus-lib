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
package de.mhus.lib.tests;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.TestInfo;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MXml;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.MUri;

public class TestUtil {

    // not working
    public static void configureApacheCommonLogging(String logger, Level level) {
        System.setProperty(
                "org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");

        String l = "FATAL";
        switch (level.getName()) {
            case "INFO":
                l = "INFO";
                break;
            case "WARNING":
                l = "WARN";
                break;
            case "FINE":
                l = "DEBUG";
                break;
            case "FINER":
                l = "TRACE";
                break;
            case "SEVERE":
                l = "WARN";
                break;
        }
        if (logger == null) System.setProperty("org.apache.commons.logging", l);
        else System.setProperty("org.apache.commons.logging." + logger, l);
    }

    public static void configureJavaLogger(String name, Level level) {
        Logger logger = Logger.getLogger(name);
        logger.setLevel(level);
    }

    public static void configureJavaLogger(Level level) {

        System.setProperty(
                "java.util.logging.SimpleFormatter.format",
                "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %4$-7s [%3$s] (%2$s) %5$s %6$s%n");

        Logger rootLogger = Logger.getLogger("");
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(level);
        }
        // Set root logger level
        rootLogger.setLevel(level);
    }

    public static void enableDebug() {
        MApi.setDirtyTrace(false);
        MApi.get().getLogFactory().setDefaultLevel(Log.LEVEL.DEBUG);
    }

    public static String getPluginVersion(String uriStr) {
        MUri uri = MUri.toUri(uriStr);
        String[] parts = uri.getPath().split("/");
        return parts[2];
    }

    public static String conrentVersion()
            throws ParserConfigurationException, SAXException, IOException {
        Document doc = MXml.loadXml(new File("pom.xml"));
        String version = MXml.getValue(doc.getDocumentElement(), "/parent/version", "");
        return version;
    }

    public static void start(TestInfo testInfo) {
        if (testInfo == null) {
            System.out.println(">>> unknown");
            return;
        }
        Optional<Class<?>> clazz = testInfo.getTestClass();
        Optional<Method> method = testInfo.getTestMethod();
        System.out.println(
                ">>> "
                        + (clazz == null || clazz.isEmpty() ? "?" : clazz.get().getCanonicalName())
                        + "::"
                        + (method == null || method.isEmpty() ? "?" : method.get().getName()));
    }

    public static void stop(TestInfo testInfo) {
        if (testInfo == null) {
            System.out.println("<<< unknown");
            return;
        }
        Optional<Class<?>> clazz = testInfo.getTestClass();
        Optional<Method> method = testInfo.getTestMethod();
        System.out.println(
                "<<< "
                        + (clazz == null || clazz.isEmpty() ? "?" : clazz.get().getCanonicalName())
                        + "::"
                        + (method == null || method.isEmpty() ? "?" : method.get().getName()));
    }
}
