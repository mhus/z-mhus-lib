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
package de.mhus.lib.core.node;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.mhus.lib.core.yaml.MYaml;
import de.mhus.lib.core.yaml.YElement;
import de.mhus.lib.core.yaml.YList;
import de.mhus.lib.core.yaml.YMap;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.TooDeepStructuresException;

public class YamlNodeBuilder extends INodeBuilder {

    @Override
    public INode read(InputStream is) {
        YMap itemY = MYaml.load(is);
        MNode itemC = new MNode();
        if (itemY.isList()) {
            NodeList arrayC = itemC.createArray(INode.NAMELESS_VALUE);
            fill(arrayC, new YList(itemY.getObject()), 0);
        } else if (itemY.isMap()) fill(itemC, itemY, 0);
        return itemC;
    }

    private void fill(INode elemC, YMap elemY, int level) {
        if (level > 100) throw new TooDeepStructuresException();

        for (String key : elemY.getKeys()) {
            if (elemY.isList(key)) {
                NodeList arrayC = elemC.createArray(key);
                fill(arrayC, elemY.getList(key), level + 1);
            } else if (elemY.isMap(key)) {
                INode objC = elemC.createObject(key);
                YMap objY = elemY.getMap(key);
                fill(objC, objY, level + 1);
            } else {
                elemC.put(key, elemY.getObject(key));
            }
        }
    }

    private void fill(NodeList listC, YList listY, int level) {
        if (level > 100) throw new TooDeepStructuresException();

        for (YElement itemY : listY) {
            INode itemC = listC.createObject();
            if (itemY.isMap()) {
                fill(itemC, itemY.asMap(), level + 1);
            } else if (itemY.isList()) {
                // nameless list in list - not really supported - but ...
                NodeList arrayY2 = itemC.createArray(INode.NAMELESS_VALUE);
                fill(arrayY2, itemY.asList(), level + 1);
            } else {
                itemC.put(INode.NAMELESS_VALUE, itemY.getObject());
            }
        }
    }

    @Override
    public void write(INode node, OutputStream os) throws MException {
        YElement elemY = create(node, 0);
        try {
            MYaml.write(elemY, os);
        } catch (IOException e) {
            throw new MException(e);
        }
    }

    private YElement create(INode elemC, int level) {

        if (level > 100) throw new TooDeepStructuresException();

        if (elemC.containsKey(INode.NAMELESS_VALUE)) {
            if (elemC.isArray(INode.NAMELESS_VALUE)) {
                YList out = MYaml.createList();
                for (INode itemC : elemC.getArrayOrNull(INode.NAMELESS_VALUE)) {
                    YElement itemY = create(itemC, level + 1);
                    out.add(itemY);
                }
                return out;
            } else if (elemC.isObject(INode.NAMELESS_VALUE)) {
                return create(elemC.getObjectOrNull(INode.NAMELESS_VALUE), level + 1);
            } else return new YElement(elemC.get(INode.NAMELESS_VALUE));
        }

        YMap elemY = MYaml.createMap();
        for (String key : elemC.getPropertyKeys()) {
            elemY.put(key, new YElement(elemC.getProperty(key)));
        }

        for (String key : elemC.getArrayKeys()) {
            YList listY = MYaml.createList();
            for (INode itemC : elemC.getArrayOrNull(key)) {
                YElement itemY = create(itemC, level + 1);
                listY.add(itemY);
            }
            elemY.put(key, listY);
        }

        for (String key : elemC.getObjectKeys()) {
            INode itemC = elemC.getObjectOrNull(key);
            YElement itemY = create(itemC, level + 1);
            elemY.put(key, itemY);
        }

        return elemY;
    }
}
