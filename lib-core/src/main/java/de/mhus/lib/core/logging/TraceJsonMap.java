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

import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.opentracing.propagation.TextMap;

public class TraceJsonMap implements TextMap {

    private ObjectNode node;
    private String prefix;

    public TraceJsonMap(ObjectNode node, String prefix) {
        this.node = node;
        this.prefix = prefix;
    }

    @Override
    public Iterator<Entry<String, String>> iterator() {
        final Iterator<Entry<String, JsonNode>> iter = node.fields();
        return new Iterator<Entry<String, String>>() {

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Entry<String, String> next() {
                final Entry<String, JsonNode> entry = iter.next();
                return new Entry<String, String>() {

                    @Override
                    public String getKey() {
                        return entry.getKey();
                    }

                    @Override
                    public String getValue() {
                        return entry.getValue().asText();
                    }

                    @Override
                    public String setValue(String value) {
                        return null;
                    }
                };
            }
        };
    }

    @Override
    public void put(String key, String value) {
        node.put(prefix + key, value);
    }
}
