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
package de.mhus.lib.core.operation.util;

import java.util.Map;
import java.util.Set;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.operation.Operation;
import de.mhus.lib.core.operation.Successful;

public class SuccessfulMap extends Successful {

    public SuccessfulMap(Operation operation) {
        super(operation, OK);
        setResultNode(new MProperties());
    }
    
    public SuccessfulMap(Operation operation, String msg) {
        super(operation, msg);
        setResultNode(new MProperties());
    }

    public SuccessfulMap(String path, String msg, int rc, String... keyValues) {
        super(path, msg, rc, keyValues);
    }

    public SuccessfulMap(Operation operation, String msg, int rc, String... keyValues) {
        super(operation.getDescription().getPath(), msg, rc, keyValues);
    }

    public Map<String, Object> getMap() {
        return getResultAsMap();
    }

    public void put(String key, Object value) {
        getMap().put(key, value);
    }

    public Object get(String key) {
        return getMap().get(key);
    }

    public void remove(String key) {
        getMap().remove(key);
    }

    public Set<String> keySet() {
        return getMap().keySet();
    }

    public int size() {
        return getMap().size();
    }
}
