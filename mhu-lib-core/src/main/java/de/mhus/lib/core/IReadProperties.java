/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

import de.mhus.lib.errors.MException;

public interface IReadProperties {

	String getString(String name, String def);

	String getString(String name) throws MException;

	boolean getBoolean(String name, boolean def);

	boolean getBoolean(String name) throws MException;

	int getInt(String name, int def);

	long getLong(String name, long def);

	float getFloat(String name, float def);

	double getDouble(String name, double def);

	Calendar getCalendar(String name) throws MException;

	Date getDate(String name);

	Number getNumber(String name, Number def);

	boolean isProperty(String name);

	Set<String> keys();

	Object get(Object name);

	Object getProperty(String name);

	boolean containsValue(Object value);

    boolean containsKey(Object key);
    
	Collection<Object> values();

	Set<Entry<String, Object>> entrySet();

}
