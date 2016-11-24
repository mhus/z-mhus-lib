package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.definition.FmElement;
import de.mhus.lib.form.definition.FmText;

public class ParameterDefinitions extends TreeMap<String, ParameterDefinition>{

	private static final long serialVersionUID = 1L;

	public static ParameterDefinitions create(Collection<String> definitions) {
		ParameterDefinitions out = new ParameterDefinitions();
		for (String line : definitions) {
			ParameterDefinition def = new ParameterDefinition(line);
			out.put(def.getName(), def);
		}
		return out;
	}

	public static ParameterDefinitions create(IConfig form) throws MException {
		ParameterDefinitions out = new ParameterDefinitions();
		collect(form, out);
		return out;
	}

	private static void collect(IConfig form, ParameterDefinitions out) throws MException {
		
		// TODO need more transform options
		if (form instanceof FmElement) {
			String name = form.getString("name");
			String type = form.getString("type", null);
			ParameterDefinition def = new ParameterDefinition(name + (type != null ? ",type:" + type : ""));
			out.put(def.getName(), def);
		}
		
		for (IConfig node : form.getNodes()) {
			collect(node, out);
		}
	}


}
