package de.mhus.lib.core.util;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

	public static Map<String,ParameterDefinition> createDefinitions(Collection<String> definitions) {
		HashMap<String,ParameterDefinition> out = new HashMap<>();
		for (String line : definitions) {
			ParameterDefinition def = new ParameterDefinition(line);
			out.put(def.getName(), def);
		}
		
		return out;
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
			default:
				log.d("Unknown Type",name,type);
			}
			return object;
	}
	
}
