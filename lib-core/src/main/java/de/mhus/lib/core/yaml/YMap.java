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
package de.mhus.lib.core.yaml;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.util.EmptySet;

public class YMap extends YElement {

    public YMap(Object obj) {
        super(obj);
    }

    @SuppressWarnings("unchecked")
    public Set<String> getKeys() {
        if (getObject() == null) return new EmptySet<>();
        return ((Map<String, Object>) getObject()).keySet();
    }

    @SuppressWarnings("unchecked")
    public YMap getMap(String key) {
        if (getObject() == null) return null;
        Object ret = ((Map<String, Object>) getObject()).get(key);
        if (ret == null) return null;
        return new YMap(ret);
    }

    @SuppressWarnings("unchecked")
    public YList getList(String key) {
        if (getObject() == null) return null;
        Object ret = ((Map<String, Object>) getObject()).get(key);
        if (ret == null) return null;
        return new YList(ret);
    }

    public String getString(String key) {
        return getString(key, null);
    }

    @SuppressWarnings("unchecked")
    public String getString(String key, String def) {
        if (getObject() == null) return def;
        Object val = ((Map<String, Object>) getObject()).get(key);
        if (val == null) return def;
        if (val instanceof String) return (String) val;
        return String.valueOf(val);
    }

    @SuppressWarnings("unchecked")
    public String[] getStringArray(String key) {
        if (getObject() == null) return new String[0];
        Object val = ((Map<String, Object>) getObject()).get(key);
        if (val instanceof List) {
            return getList(key).toStringList().toArray(new String[0]);
        }
        return new String[] {String.valueOf(val)};
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    @SuppressWarnings("unchecked")
    public boolean getBoolean(String key, boolean def) {
        if (getObject() == null) return def;
        Object val = ((Map<String, Object>) getObject()).get(key);
        if (val == null) return def;
        if (val instanceof Boolean) return (Boolean) val;
        return MCast.toboolean(val, def);
    }

    @SuppressWarnings("unchecked")
    public boolean isEmpty() {
        return getObject() == null || ((Map<String, Object>) getObject()).size() == 0;
    }

    @SuppressWarnings("unchecked")
    public boolean isString(String key) {
        if (getObject() == null) return false;
        Object val = ((Map<String, Object>) getObject()).get(key);
        return val instanceof String;
    }

    @SuppressWarnings("unchecked")
    public boolean isList(String key) {
        if (getObject() == null) return false;
        Object val = ((Map<String, Object>) getObject()).get(key);
        return val instanceof List;
    }

    @SuppressWarnings("unchecked")
    public boolean isMap(String key) {
        if (getObject() == null) return false;
        Object val = ((Map<String, Object>) getObject()).get(key);
        return val instanceof Map;
    }

    @SuppressWarnings("unchecked")
    public Object getObject(String key) {
        if (getObject() == null) return null;
        return ((Map<String, Object>) getObject()).get(key);
    }

    @SuppressWarnings("unchecked")
    public YElement getElement(String key) {
        if (getObject() == null) return null;
        Object val = ((Map<String, Object>) getObject()).get(key);
        if (val == null) return null;
        return new YElement(val);
    }

    public int getInteger(String key) {
        return getInteger(key, 0);
    }

    @SuppressWarnings("unchecked")
    public int getInteger(String key, int def) {
        if (getObject() == null) return def;
        Object val = ((Map<String, Object>) getObject()).get(key);
        if (val == null) return def;
        if (val instanceof Number) return ((Number) val).intValue();
        return MCast.toint(val, def);
    }

    @SuppressWarnings("unchecked")
    public boolean isInteger(String key) {
        if (getObject() == null) return false;
        Object val = ((Map<String, Object>) getObject()).get(key);
        if (val == null) return false;
        return val instanceof Number;
    }

    @SuppressWarnings("unchecked")
    public void put(String key, YElement elem) {
        if (elem == null || elem.getObject() == null)
            ((Map<String, Object>) getObject()).remove(key);
        else ((Map<String, Object>) getObject()).put(key, elem.getObject());
    }

    @Override
    public String toString() {
        return getObject() == null ? null : getObject().toString();
    }
}
