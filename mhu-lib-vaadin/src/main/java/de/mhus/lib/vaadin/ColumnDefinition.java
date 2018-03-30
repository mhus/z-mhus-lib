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
package de.mhus.lib.vaadin;

import java.util.Properties;

public class ColumnDefinition {

	private String id;
	private Class<?> type;
	private Object def;
	private String title;
	private boolean showByDefault;
	private Properties properties = null;

	public ColumnDefinition(String id, Class<?> type, Object def, String title, boolean showByDefault) {
		this(id, type, def, title, showByDefault, null);
	}
	
	public ColumnDefinition(String id, Class<?> type, Object def, String title, boolean showByDefault, Properties properties) {
		this.id = id;
		this.type = type;
		this.def = def;
		this.title = title;
		this.showByDefault = showByDefault;
		this.properties = properties;
	}
	
	public String getId() {
		return id;
	}

	public Class<?> getType() {
		return type;
	}

	public Object getDefaultValue() {
		return def;
	}

	public String getTitle() {
		return title;
	}

	public boolean isShowByDefault() {
		return showByDefault;
	}

	public boolean hasPropertiy(String key) {
		return properties != null && properties.containsKey(key);
	}
	
	public String getProperty(String key, String def) {
		if (properties == null) return def;
		return properties.getProperty(key,def);
	}
}
