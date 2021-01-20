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
package de.mhus.lib.core;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import de.mhus.lib.errors.MException;

public interface IReadProperties {

    String getString(String name, String def);

    String getStringOrCreate(String name, Function<String, String> def);

    String getString(String name) throws MException;

    boolean getBoolean(String name, boolean def);

    //    boolean getBooleanOrCreate(String name, Function<String, Boolean> def);

    boolean getBoolean(String name) throws MException;

    int getInt(String name, int def);

    //    int getIntOrCreate(String name, Function<String, Integer> def);

    long getLong(String name, long def);

    //    long getLongOrCreate(String name, Function<String, Long> def);

    float getFloat(String name, float def);

    //    float getFloatOrCreate(String name, Function<String, Float> def);

    double getDouble(String name, double def);

    //    double getDoubleOrCreate(String name, Function<String, Double> def);

    Calendar getCalendar(String name) throws MException;

    //    Calendar getCalendarOrCreate(String name, Function<String, Calendar> def) throws
    // MException;

    Date getDate(String name);

    //    Date getDateOrCreate(String name, Function<String, Date> def);

    Number getNumber(String name, Number def);

    //    Number getNumberOrCreate(String name, Function<String, Number> def);

    boolean isProperty(String name);

    Set<String> keys();

    Object get(Object name);

    Object getProperty(String name);

    boolean containsValue(Object value);

    boolean containsKey(Object key);

    Collection<Object> values();

    Set<Entry<String, Object>> entrySet();
}
