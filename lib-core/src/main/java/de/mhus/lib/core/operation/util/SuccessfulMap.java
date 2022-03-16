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

import java.util.Set;

import de.mhus.lib.basics.RC;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.operation.Operation;
import de.mhus.lib.core.operation.Successful;

public class SuccessfulMap extends Successful {


    public SuccessfulMap(Operation operation, String msg, String... keyValues) {
        this(operation.getDescription().getPath(), RC.OK, msg, keyValues);
    }

    @SuppressWarnings("deprecation")
    public SuccessfulMap(String path, int rc, String msg, String... keyValues) {
        super(path, rc, msg);
        MProperties r = new MProperties();
        if (keyValues != null) {
            for (int i = 0; i < keyValues.length - 1; i += 2)
                if (keyValues.length > i + 1) r.put(keyValues[i], keyValues[i + 1]);
        }
        setResult(r);
    }

    @SuppressWarnings("deprecation")
    public SuccessfulMap(Operation operation, int rc, String msg, String... keyValues) {
        super(operation, rc, msg);
        MProperties r = new MProperties();
        if (keyValues != null) {
            for (int i = 0; i < keyValues.length - 1; i += 2)
                if (keyValues.length > i + 1) r.put(keyValues[i], keyValues[i + 1]);
        }
        setResult(r);
    }
    
    public SuccessfulMap(Operation operation) {
        this(operation, OK);
    }
    
    public SuccessfulMap(Operation operation, String msg) {
        this(operation, RC.OK, msg);
    }

    @SuppressWarnings({ "deprecation" })
    public IProperties getMap() {
        return (IProperties)getResult();
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
