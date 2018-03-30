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
package de.mhus.lib.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.mhus.lib.core.IReadProperties;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.errors.MException;

public class ParameterDefinition {
	private static Log log = Log.getLog(ParameterDefinition.class);

	private String name;
	private String type;
	private String def = null;
	private boolean mandatory;
	private String mapping;
	private String format;
	private IReadProperties properties;
	
	public ParameterDefinition(String line) {
		if (MString.isIndex(line, ',')) {
			name = MString.beforeIndex(line, ',');
			line = MString.afterIndex(line, ',');
			
			properties = MProperties.explodeToMProperties(line.split(","), ':');
			
		} else {
			name = line;
			if (name.startsWith("*")) {
				mandatory = true;
				name = name.substring(1);
			}
			type = "string";
			return;
		}
		
		if (name.startsWith("*")) {
			mandatory = true;
			name = name.substring(1);
		}
			
		type = properties.getString("type", "");
		mandatory = properties.getBoolean("mandatory", mandatory);
		def = properties.getString("default", null);
		mapping = properties.getString("mapping",null);
		format = properties.getString("format", null);
		
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getDef() {
		return def;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public String getMapping() {
		return mapping;
	}

	public String getFormat() {
		return format;
	}

	public IReadProperties getProperties() {
		return properties;
	}

	public Object transform(Object object) throws MException {
			switch (type) {
			case "int":
			case "integer":
				return MCast.toint(object, MCast.toint(def,0));
			case "long":
				return MCast.tolong(object, MCast.tolong(def,0));
			case "bool":
			case "boolean":
				return MCast.toboolean(object, MCast.toboolean(def,false));
			case "datestring": {
				Date date = MCast.toDate(object, MCast.toDate(def,null));
				if (date == null) return "";
				return  new SimpleDateFormat(format).format(date);
			}
			case "date": {
				Date date = MCast.toDate(object, MCast.toDate(def,null));
				if (date == null) return "";
				return date;
			}
			case "enum": {
				String[] parts = def.split(",");
				String val = String.valueOf(object).toLowerCase();
				for (String p : parts)
					if (val.equals(p.toLowerCase())) return p;
				if (isMandatory()) throw new MException("field is mandatory", name);
				return "";
			}
			case "string":
			case "text": {
				return String.valueOf(object);
			}
			default:
				log.d("Unknown Type",name,type);
			}
			return object;
	}
	
	@Override
	public String toString() {
		return name + ":" + type;
	}
	
}
