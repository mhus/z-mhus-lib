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

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public interface IProperties extends IReadProperties, Map<String,Object>, Serializable, Iterable<Map.Entry<String,Object>> {


	void setString(String name, String value);

	void setInt(String name, int value);

	void setLong(String name, long value);

	void setDouble(String name, double value);

	void setFloat(String name, float value);

	void setBoolean(String name, boolean value);

	void setCalendar(String name, Calendar value);

	void setDate(String name, Date value);

	void setNumber(String name, Number value);

	void removeProperty(String key);

	boolean isEditable();
	
	@Override
	void clear();

	String getFormatted(String name, String def, Object ... values );

}
