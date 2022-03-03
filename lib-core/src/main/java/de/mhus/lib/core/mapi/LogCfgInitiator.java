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
package de.mhus.lib.core.mapi;

import java.io.PrintStream;
import java.util.Collection;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MCollection;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.cfg.CfgInitiator;
import de.mhus.lib.core.logging.ConsoleFactory;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.core.logging.LogFactory;
import de.mhus.lib.core.logging.MLogFactory;
import de.mhus.lib.core.logging.MutableParameterMapper;
import de.mhus.lib.core.logging.ParameterEntryMapper;
import de.mhus.lib.core.logging.ParameterMapper;
import de.mhus.lib.core.node.INode;

public class LogCfgInitiator implements CfgInitiator {

    private static PrintStream err;
    private static PrintStream out;

    static {
        err = System.err;
        out = System.out;
    }

    @Override
    public void doInitialize(IApiInternal internal, MCfgManager manager, INode config) {

        INode system = manager.getCfg("system");

        if (system == null) system = manager.getConfigFactory().create(); // empty

        internal.getLogTrace().clear();
        for (String p : system.getPropertyKeys()) {
            if (p.startsWith("TRACE.")) internal.getLogTrace().add(p.substring(6));
        }

        MLogFactory mlogFactory = null;
        try {
            String key = M.PROP_LOG_MLOG_FACTORY_CLASS;
            String name = system.getString(key, null);
            if (MString.isEmpty(name)) name = System.getProperty(M.PROP_PREFIX + key);
            if (MString.isSet(name)) {
                mlogFactory =
                        (MLogFactory)
                                Class.forName(name.trim()).getDeclaredConstructor().newInstance();
            }
        } catch (Throwable t) {
            MApi.dirtyLogDebug(t);
        }
        if (mlogFactory != null) internal.setMLogFactory(mlogFactory);

        LogFactory logFactory = null;
        try {
            String key = M.PROP_LOG_FACTORY_CLASS;
            String name = system.getString(key, null);
            if (MString.isEmpty(name)) name = System.getProperty(M.PROP_PREFIX + key);
            if (MString.isSet(name)) {
                logFactory =
                        (LogFactory)
                                Class.forName(name.trim()).getDeclaredConstructor().newInstance();
            }
        } catch (Throwable t) {
            MApi.dirtyLogDebug(t);
        }
        if (logFactory == null) logFactory = new ConsoleFactory();

        try {
            String key = M.PROP_LOG_LEVEL_MAPPER_CLASS;
            String name = system.getString(key, null);
            if (MString.isEmpty(name)) name = System.getProperty(M.PROP_PREFIX + key);
        } catch (Throwable t) {
            MApi.dirtyLogDebug(t);
        }

        try {
            String key = M.PROP_LOG_MAX_MESSAGE_SIZE;
            String size = system.getString(key, null);
            if (size != null) {
                logFactory.setMaxMessageSize(Integer.valueOf(size));
                Log.setMaxMsgSize(Integer.valueOf(size));
            }
        } catch (Throwable t) {
            MApi.dirtyLogDebug(t);
        }

        try {
            String key = M.PROP_LOG_MAX_MESSAGE_SIZE_EXCEPTIONS;
            String val = system.getString(key, null);
            if (val != null) {
                logFactory.setMaxMessageSizeExceptions(MCollection.toList(val.split("\\|")));
            }
        } catch (Throwable t) {
            MApi.dirtyLogDebug(t);
        }

        try {
            String key = M.PROP_LOG_PARAMETER_MAPPER_CLASS;
            String name = system.getString(key, null);
            if (MString.isEmpty(name)) name = System.getProperty(M.PROP_PREFIX + key);
            if (MString.isSet(name)) {
                logFactory.setParameterMapper(
                        (ParameterMapper)
                                Class.forName(name.trim()).getDeclaredConstructor().newInstance());
            }
        } catch (Throwable t) {
            MApi.dirtyLogDebug(t);
        }

        if (logFactory.getParameterMapper() != null
                && logFactory.getParameterMapper() instanceof MutableParameterMapper) {
            try {
                Collection<INode> mappers =
                        system.getArrayOrCreate(M.PROP_LOG_PARAMETER_ENTRY_MAPPER_CLASS);
                if (mappers.size() > 0)
                    ((MutableParameterMapper) logFactory.getParameterMapper()).clear();
                for (INode mapper : mappers) {
                    String name = mapper.getString("name");
                    String clazz = mapper.getString("class");
                    if (MString.isSet(name) && MString.isSet(clazz)) {
                        ParameterEntryMapper inst = null;
                        try {
                            inst =
                                    (ParameterEntryMapper)
                                            Class.forName(clazz.trim())
                                                    .getDeclaredConstructor()
                                                    .newInstance();
                        } catch (Throwable t) {
                            MApi.dirtyLogDebug(
                                    "LogCfgInitiator:UseProxy", name, clazz, t.getMessage());
                            inst = new ParameterEntryMapperProxy(clazz);
                        }
                        ((MutableParameterMapper) logFactory.getParameterMapper()).put(name, inst);
                    }
                }
            } catch (Throwable t) {
                MApi.dirtyLogDebug(t);
            }
        }

        try {
            String key = M.PROP_LOG_CONSOLE_REDIRECT;
            String name = system.getString(key, null);
            if (MString.isEmpty(name)) name = System.getProperty(M.PROP_PREFIX + key);
            if (MString.isSet(name)) {
                if ("true".equals(name)) {
                    System.setErr(new SecureStreamToLogAdapter(LEVEL.ERROR, err));
                    System.setOut(new SecureStreamToLogAdapter(LEVEL.INFO, out));
                }
            }
        } catch (Throwable t) {
            MApi.dirtyLogDebug(t);
        }

        try {
            String key = M.PROP_LOG_LEVEL;
            String value = system.getString(key, null);
            if (MString.isEmpty(value)) value = System.getProperty(M.PROP_PREFIX + key);
            if (MString.isSet(value)) {
                logFactory.setDefaultLevel(Log.LEVEL.valueOf(value.toUpperCase()));
            }
        } catch (Throwable t) {
            MApi.dirtyLogDebug(t);
        }

        try {
            String key = M.PROP_LOG_VERBOSE;
            boolean value = system.getBoolean(key, false);
            if (!value) value = MCast.toboolean(System.getProperty(M.PROP_PREFIX + key), false);
            Log.setVerbose(value);
        } catch (Throwable t) {
            MApi.dirtyLogDebug(t);
        }

        internal.setLogFactory(logFactory);

        MApi.updateLoggers();
    }
}
