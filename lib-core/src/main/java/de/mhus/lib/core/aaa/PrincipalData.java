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
package de.mhus.lib.core.aaa;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.errors.NotSupportedException;

public class PrincipalData implements Map<String, String>, Serializable {

    private static final long serialVersionUID = 1L;
    public static final String DISPLAY_NAME = "_shiro.displayName";
    public static final String NAME = "_shiro.name";
    public static final String SESSION_KEY = "_shiro.principalData";
    //    private static final String READ_ONLY = "_ro";

    protected Map<String, String> data = null;

    public PrincipalData() {}

    public PrincipalData(Map<String, String> data) {
        this.data = new HashMap<>(data);
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return data.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return data.containsValue(value);
    }

    @Override
    public String get(Object key) {
        return data.get(key);
    }

    @Override
    public String put(String key, String value) {
        throw new NotSupportedException();
    }

    @Override
    public String remove(Object key) {
        throw new NotSupportedException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        throw new NotSupportedException();
    }

    @Override
    public void clear() {
        throw new NotSupportedException();
    }

    @Override
    public Set<String> keySet() {
        return data.keySet();
    }

    @Override
    public Collection<String> values() {
        return data.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        throw new NotSupportedException();
    }

    @Override
    public boolean equals(Object o) {
        return data.equals(o);
    }

    @Override
    public String getOrDefault(Object key, String defaultValue) {
        return data.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super String> action) {
        data.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super String, ? extends String> function) {
        throw new NotSupportedException();
    }

    @Override
    public String putIfAbsent(String key, String value) {
        throw new NotSupportedException();
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw new NotSupportedException();
    }

    @Override
    public boolean replace(String key, String oldValue, String newValue) {
        throw new NotSupportedException();
    }

    @Override
    public String replace(String key, String value) {
        throw new NotSupportedException();
    }

    @Override
    public String computeIfAbsent(
            String key, Function<? super String, ? extends String> mappingFunction) {
        throw new NotSupportedException();
    }

    @Override
    public String computeIfPresent(
            String key,
            BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
        throw new NotSupportedException();
    }

    @Override
    public String compute(
            String key,
            BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
        throw new NotSupportedException();
    }

    @Override
    public String merge(
            String key,
            String value,
            BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
        throw new NotSupportedException();
    }

    public String getDisplayName() {
        return get(DISPLAY_NAME);
    }

    public String getName() {
        return get(NAME);
    }

    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        s.writeObject(data);
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        data = (Map<String, String>) s.readObject();
    }

    @Override
    public String toString() {
        return MSystem.toString(this, data);
    }
}
