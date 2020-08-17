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
package de.mhus.lib.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.mhus.lib.core.yaml.MYaml;
import de.mhus.lib.core.yaml.YElement;
import de.mhus.lib.core.yaml.YList;
import de.mhus.lib.core.yaml.YMap;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.TooDeepStructuresException;

public class YamlConfigBuilder extends IConfigBuilder {

    @Override
    public IConfig read(InputStream is) {
        YMap itemY = MYaml.load(is);
        MConfig itemC = new MConfig();
        fill(itemC, itemY, 0);
        return itemC;
    }

    private void fill(IConfig elemC, YMap elemY, int level) {
        if (level > 100) throw new TooDeepStructuresException();

        for (String key : elemY.getKeys()) {
            if (elemY.isList(key)) {
                ConfigList arrayC = elemC.createArray(key);
                fill(arrayC, elemY.getList(key), level + 1);
            } else if (elemY.isMap(key)) {
                IConfig objC = elemC.createObject(key);
                YMap objY = elemY.getMap(key);
                fill(objC, objY, level + 1);
            } else {
                elemC.put(key, elemY.getObject(key));
            }
        }
    }

    private void fill(ConfigList listC, YList listY, int level) {
        if (level > 100) throw new TooDeepStructuresException();

        for (YElement itemY : listY) {
            IConfig itemC = listC.createObject();
            if (itemY.isMap()) {
                fill(itemC, itemY.asMap(), level + 1);
            } else if (itemY.isList()) {
                // nameless list in list - not really supported - but ...
                ConfigList arrayY2 = itemC.createArray(IConfig.NAMELESS_VALUE);
                fill(arrayY2, itemY.asList(), level + 1);
            } else {
                itemC.put(IConfig.NAMELESS_VALUE, itemY.getObject());
            }
        }
    }

    @Override
    public void write(IConfig config, OutputStream os) throws MException {
        YElement elemY = create(config, 0);
        try {
            MYaml.write(elemY, os);
        } catch (IOException e) {
            throw new MException(e);
        }
    }

    private YElement create(IConfig elemC, int level) {

        if (level > 100) throw new TooDeepStructuresException();

        if (elemC.containsKey(IConfig.NAMELESS_VALUE)) {
            if (elemC.isArray(IConfig.NAMELESS_VALUE)) {
                YList out = MYaml.createList();
                for (IConfig itemC : elemC.getArrayOrNull(IConfig.NAMELESS_VALUE)) {
                    YElement itemY = create(itemC, level + 1);
                    out.add(itemY);
                }
                return out;
            } else if (elemC.isObject(IConfig.NAMELESS_VALUE)) {
                return create(elemC.getObjectOrNull(IConfig.NAMELESS_VALUE), level + 1);
            } else return new YElement(elemC.get(IConfig.NAMELESS_VALUE));
        }

        YMap elemY = MYaml.createMap();
        for (String key : elemC.getPropertyKeys()) {
            elemY.put(key, new YElement(elemC.getProperty(key)));
        }

        for (String key : elemC.getArrayKeys()) {
            YList listY = MYaml.createList();
            for (IConfig itemC : elemC.getArrayOrNull(key)) {
                YElement itemY = create(itemC, level + 1);
                listY.add(itemY);
            }
            elemY.put(key, listY);
        }

        for (String key : elemC.getObjectKeys()) {
            IConfig itemC = elemC.getObjectOrNull(key);
            YElement itemY = create(itemC, level + 1);
            elemY.put(key, itemY);
        }

        return elemY;
    }
}
