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
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.mhus.lib.core.MJson;
import de.mhus.lib.core.util.MIterable;
import de.mhus.lib.core.util.NullValue;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotSupportedException;
import de.mhus.lib.errors.TooDeepStructuresException;

public class JsonConfigBuilder extends IConfigBuilder {

    @Override
    public IConfig read(InputStream is) throws MException {
        try {
            JsonNode docJ = MJson.load(is);
            return fromJson(docJ);
        } catch (IOException e) {
            throw new MException(e);
        }
    }

    public IConfig fromJson(JsonNode docJ) throws MException {
        MConfig config = new MConfig();
        if (docJ.isArray()) {
            ConfigList array = config.createArray(IConfig.NAMELESS_VALUE);
            for (JsonNode itemJ : docJ) {
                IConfig obj = array.createObject();
                fill(obj, "", itemJ, 0);
            }
        } else if (docJ.isObject()) {
            fill(config, "", docJ, 0);
        } else if (docJ.isValueNode()) {
            // TODO separate for each type
            config.setString(IConfig.NAMELESS_VALUE, docJ.asText());
        } else {
            throw new MException("Unknown basic json object type");
        }

        return config;
    }

    private void fill(IConfig config, String name, JsonNode json, int level) {

        if (level > 100) throw new TooDeepStructuresException();

        if (json.isValueNode()) {
            config.put(IConfig.NAMELESS_VALUE, json.asText());
            return;
        }

        for (Map.Entry<String, JsonNode> itemJ : new MIterable<>(json.fields())) {
            if (itemJ.getValue().isArray()) {
                ConfigList array = config.createArray(itemJ.getKey());
                for (JsonNode item2J : itemJ.getValue()) {
                    IConfig obj = array.createObject();
                    fill(obj, itemJ.getKey(), item2J, level + 1);
                }
            } else if (itemJ.getValue().isObject()) {
                IConfig obj = config.createObject(itemJ.getKey());
                fill(obj, itemJ.getKey(), itemJ.getValue(), level + 1);
            } else config.put(itemJ.getKey(), itemJ.getValue().asText());
        }
    }

    @Override
    public void write(IConfig config, OutputStream os) throws MException {
        try {
            ObjectNode objectJ = writeToJsonNode(config);
            MJson.save(objectJ, os);
        } catch (IOException e) {
            throw new MException(e);
        }
    }

    public ObjectNode writeToJsonNode(IConfig config) {
        if (config.isArray(IConfig.NAMELESS_VALUE)) {
            //          ArrayNode arrayJ = MJson.createArrayNode();
            //          for (IConfig itemC : config.getArrayOrNull(IConfig.NAMELESS_VALUE)) {
            //
            //          }
            //          MJson.save(arrayJ, os);
            throw new NotSupportedException("first config node as array is not supported");
        } else {
            ObjectNode objectJ = MJson.createObjectNode();
            fill(objectJ, config, 0);
            return objectJ;
        }
    }

    private void fill(ObjectNode objectJ, IConfig itemC, int level) {
        if (level > 100) throw new TooDeepStructuresException();

        for (String key : itemC.keys()) {
            if (itemC.isArray(key)) {
                ArrayNode arrJ = objectJ.putArray(key);
                for (IConfig arrC : itemC.getArrayOrNull(key)) {
                    ObjectNode newJ = arrJ.addObject();
                    fill(newJ, arrC, level + 1);
                }
            } else if (itemC.isObject(key)) {
                ObjectNode newJ = MJson.createObjectNode();
                fill(newJ, itemC.getObjectOrNull(key), level + 1);
                objectJ.set(key, newJ);
            } else if (itemC.get(key) instanceof NullValue) objectJ.putNull(key);
            else objectJ.put(key, itemC.getString(key, null));
        }
    }
}
