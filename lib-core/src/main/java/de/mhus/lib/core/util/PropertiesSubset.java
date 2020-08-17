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
package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.errors.NotSupportedException;

public class PropertiesSubset extends AbstractProperties {

    private static final long serialVersionUID = 1L;
    private IProperties parent;
    private String prefix;
    private boolean readonly;
    private int len;

    public PropertiesSubset(IProperties parent, String prefix) {
        this(parent, prefix, false);
        len = prefix.length();
    }

    public PropertiesSubset(IProperties parent, String prefix, boolean readonly) {
        this.parent = parent;
        this.prefix = prefix;
        this.readonly = readonly;
    }

    @Override
    public Object getProperty(String name) {
        return parent.get(prefix + name);
    }

    @Override
    public boolean isProperty(String name) {
        return parent.isProperty(prefix + name);
    }

    @Override
    public void removeProperty(String key) {
        if (readonly) throw new NotSupportedException();
        parent.removeProperty(prefix + key);
    }

    @Override
    public void setProperty(String key, Object value) {
        if (readonly) throw new NotSupportedException();
        parent.put(prefix + key, value);
    }

    @Override
    public boolean isEditable() {
        if (readonly) return false;
        return parent.isEditable();
    }

    @Override
    public Set<String> keys() {
        HashSet<String> out = new HashSet<>();
        for (String k : parent.keys()) if (k.startsWith(prefix)) out.add(k);
        return out;
    }

    @Override
    public int size() {
        int cnt = 0;
        for (String k : parent.keys()) if (k.startsWith(prefix)) cnt++;
        return cnt;
    }

    @Override
    public boolean containsValue(Object value) {
        for (java.util.Map.Entry<String, Object> entry : parent.entrySet()) {
            if (entry.getKey().startsWith(prefix)) if (value.equals(entry.getValue())) return true;
        }
        return false;
    }

    @Override
    public Collection<Object> values() {
        HashSet<Object> out = new HashSet<>();
        for (java.util.Map.Entry<String, Object> entry : parent.entrySet()) {
            if (entry.getKey().startsWith(prefix)) out.add(entry.getValue());
        }
        return out;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        HashSet<java.util.Map.Entry<String, Object>> out = new HashSet<>();
        for (java.util.Map.Entry<String, Object> entry : parent.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                out.add(new MapEntry(entry.getKey().substring(len), entry.getValue()));
            }
        }
        return out;
    }

    @Override
    public void clear() {
        for (String key : keys()) remove(key);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(prefix).append("=[");
        boolean first = true;
        for (String k : parent.keys())
            if (k.startsWith(prefix)) {
                if (first) first = false;
                else out.append(", ");
                out.append(k.substring(len)).append('=').append(parent.get(k));
            }
        out.append(']');
        return out.toString();
    }
}
