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

import java.util.Collection;
import java.util.TreeMap;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.definition.FmElement;

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
