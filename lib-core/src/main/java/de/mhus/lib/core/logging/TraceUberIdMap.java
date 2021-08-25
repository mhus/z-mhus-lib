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

import de.mhus.lib.core.util.EmptyIterator;
import io.opentracing.propagation.TextMap;

public class TraceUberIdMap implements TextMap {

    private String id;

    public TraceUberIdMap() {}

    @Override
    public Iterator<Entry<String, String>> iterator() {
        return new EmptyIterator<>();
    }

    @Override
    public void put(String key, String value) {
        if (key.equals("uber-trace-id")) id = value;
    }

    public String getId() {
        return id;
    }
}
