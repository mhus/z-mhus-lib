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
package de.mhus.lib.core.util;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import de.mhus.lib.basics.ReadOnly;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.IReadProperties;
import de.mhus.lib.errors.MException;

public class PropertiesReadOnly implements IReadProperties, ReadOnly {

    private IProperties parent;

    public PropertiesReadOnly(IProperties parent) {
        this.parent = parent;
    }

    @Override
    public Object get(Object name) {
        return parent.get(name);
    }

    @Override
    public boolean isProperty(String name) {
        return parent.isProperty(name);
    }

    @Override
    public Set<String> keys() {
        return parent.keys();
    }

    @Override
    public Object getProperty(String name) {
        return get(name);
    }

    @Override
    public boolean containsValue(Object value) {
        return parent.containsValue(value);
    }

    @Override
    public boolean containsKey(Object key) {
        return parent.containsKey(key);
    }

    @Override
    public Collection<Object> values() {
        return parent.values();
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return parent.entrySet();
    }

    @Override
    public String getString(String name, String def) {
        return parent.getString(name, def);
    }

    @Override
    public String getString(String name) throws MException {
        return parent.getString(name);
    }

    @Override
    public boolean getBoolean(String name, boolean def) {
        return parent.getBoolean(name, def);
    }

    @Override
    public boolean getBoolean(String name) throws MException {
        return parent.getBoolean(name);
    }

    @Override
    public int getInt(String name, int def) {
        return parent.getInt(name, def);
    }

    @Override
    public long getLong(String name, long def) {
        return parent.getLong(name, def);
    }

    @Override
    public float getFloat(String name, float def) {
        return parent.getFloat(name, def);
    }

    @Override
    public double getDouble(String name, double def) {
        return parent.getDouble(name, def);
    }

    @Override
    public Calendar getCalendar(String name) throws MException {
        return parent.getCalendar(name);
    }

    @Override
    public Date getDate(String name) {
        return parent.getDate(name);
    }

    @Override
    public Number getNumber(String name, Number def) {
        return parent.getNumber(name, def);
    }

    @Override
    public String getStringOrCreate(String name, Function<String, String> def) {
        return parent.getStringOrCreate(name, def);
    }

    @Override
    public Iterator<Entry<String, Object>> iterator() {
        return parent.iterator();
    }
}
