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
package de.mhus.lib.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.cfg.CfgInt;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.TooDeepStructuresException;

public class PropertiesConfigBuilder extends IConfigBuilder {

    protected static final CfgInt CFG_MAX_LEVEL =
            new CfgInt(PropertiesConfigBuilder.class, "maxLevel", 100);

    @Override
    public IConfig read(InputStream is) throws MException {
        try {
            MProperties p = MProperties.load(is);
            return readFromMap(p);
        } catch (IOException e) {
            throw new MException(e);
        }
    }

    @Override
    public void write(IConfig config, OutputStream os) throws MException {
        MProperties p = new MProperties(config);
        try {
            p.save(os);
        } catch (IOException e) {
            throw new MException(e);
        }
    }

    public IConfig readFromMap(Map<?, ?> map) {
        return readFromMap(map, 0);
    }

    public IConfig readFromCollection(Collection<?> col) {
        IConfig config = new MConfig();
        readFromCollection(config, IConfig.NAMELESS_VALUE, col, 0);
        return config;
    }

    protected void readFromCollection(IConfig config, String key, Collection<?> col, int level) {
        level++;
        if (level > CFG_MAX_LEVEL.value()) throw new TooDeepStructuresException();

        ConfigList arr = config.createArray(key);
        for (Object item : col) {
            if (item instanceof IConfig) {
                arr.add((IConfig) item);
            } else if (item instanceof Map) {
                IConfig obj = readFromMap((Map<?, ?>) item, level);
                arr.add(obj);
            } else {
                MConfig obj = new MConfig();
                obj.put(IConfig.NAMELESS_VALUE, item);
                arr.add(obj);
            }
        }
    }

    protected IConfig readFromMap(Map<?, ?> map, int level) {
        level++;
        if (level > CFG_MAX_LEVEL.value()) throw new TooDeepStructuresException();

        IConfig config = new MConfig();
        for (Entry<?, ?> entry : map.entrySet()) {
            String key = MString.valueOf(entry.getKey());
            Object val = entry.getValue();
            if (val instanceof IConfig) {
                config.addObject(key, (IConfig) val);
            } else if (val instanceof Map) {
                IConfig obj = readFromMap((Map<?, ?>) val, level);
                config.addObject(key, obj);
            } else if (val instanceof Collection) {
                readFromCollection(config, key, (Collection<?>) val, level);
            } else config.put(key, val);
        }
        return config;
    }
}
