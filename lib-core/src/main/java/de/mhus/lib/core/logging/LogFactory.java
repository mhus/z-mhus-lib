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
package de.mhus.lib.core.logging;

import java.util.WeakHashMap;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.M;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.core.mapi.MCfgManager;

@DefaultImplementation(ConsoleFactory.class)
public abstract class LogFactory {

    WeakHashMap<String, LogEngine> buffer = new WeakHashMap<String, LogEngine>();
    protected LEVEL level = LEVEL.INFO;
    // protected LevelMapper levelMapper;
    private ParameterMapper parameterMapper;
    private int maxMsgSize = 1024 * 100; // 100kb default max

    /**
     * Convenience method to derive a name from the specified class and call <code>
     * getInstance(String)</code> with it.
     *
     * @param clazz Class for which a suitable Log name will be derived
     * @return The current log engine
     */
    public LogEngine getInstance(Class<?> clazz) {
        return getInstance(clazz.getCanonicalName());
    }

    public abstract void init(IConfig config) throws Exception;

    public void init() throws Exception {
        IConfig config = M.l(MCfgManager.class).getCfg(this, null);
        init(config);
    }

    /**
     * Construct (if necessary) and return a <code>Log</code> instance, using the factory's current
     * set of configuration attributes.
     *
     * <p><strong>NOTE</strong> - Depending upon the implementation of the <code>LogFactory</code>
     * you are using, the <code>Log</code> instance you are returned may or may not be local to the
     * current application, and may or may not be returned again on a subsequent call with the same
     * name argument.
     *
     * @param name Logical name of the <code>Log</code> instance to be returned (the meaning of this
     *     name is only known to the underlying logging implementation that is being wrapped)
     * @return the log engine
     */
    public synchronized LogEngine getInstance(String name) {
        LogEngine inst = buffer.get(name);
        if (inst == null) {
            inst = createInstance(name);
            inst.doInitialize(this);
            buffer.put(name, inst);
        }
        return inst;
    }

    /**
     * Construct and return a <code>Log</code> instance, using the factory's current set of
     * configuration attributes.
     *
     * <p><strong>NOTE</strong> - Depending upon the implementation of the <code>LogFactory</code>
     * you are using, the <code>Log</code> instance you are returned may or may not be local to the
     * current application, and may or may not be returned again on a subsequent call with the same
     * name argument.
     *
     * @param name Logical name of the <code>Log</code> instance to be returned (the meaning of this
     *     name is only known to the underlying logging implementation that is being wrapped)
     * @return the log engine
     */
    public abstract LogEngine createInstance(String name);

    public LogEngine getLog(Class<?> class1) {
        return getInstance(class1);
    }

    public void setDefaultLevel(LEVEL level) {
        this.level = level;
    }

    public LEVEL getDefaultLevel() {
        return level;
    }

    //	public synchronized LevelMapper getLevelMapper(Class<? extends LevelMapper> defaultMapper) {
    //		if (levelMapper == null && defaultMapper != null) {
    //			try {
    //				levelMapper = defaultMapper.newInstance();
    //			} catch (Exception e) {
    //			}
    //		}
    //		if (defaultMapper != null && defaultMapper.isInstance(levelMapper))
    //			return levelMapper;
    //		else
    //			return null;
    //	}

    //    public LevelMapper getLevelMapper() {
    //        return levelMapper;
    //    }
    //
    //    public void setLevelMapper(LevelMapper levelMapper) {
    //        this.levelMapper = levelMapper;
    //    }

    public ParameterMapper getParameterMapper() {
        return parameterMapper;
    }

    public void setParameterMapper(ParameterMapper parameterMapper) {
        this.parameterMapper = parameterMapper;
    }

    public int getMaxMessageSize() {
        return maxMsgSize;
    }

    public void setMaxMessageSize(int max) {
        maxMsgSize = max;
    }

    //    public void update(Observable o, Object arg) {
    //        setTrace(MApi.instance().getConfig().getBoolean(name + ".TRACE" ,
    // MApi.instance().getConfig().getBoolean("TRACE",false) ));
    //    }

}
