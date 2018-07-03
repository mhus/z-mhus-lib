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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;

public class FilterRequest {

	private Map<String, String> facets;
	private String[] text;

	public FilterRequest(String[] text, Map<String, String> facets) {
		this.text = text;
		this.facets = facets;
	}
	
	public FilterRequest(String text) {
		if (text == null) return;
		LinkedList<String> lText = new LinkedList<String>();
		facets = new HashMap<String, String>();
		for (String part : text.split(" ")) {
			part = part.trim();
			if (MString.isSet(part)) {
				if (part.indexOf(':') > 0) {
					facets.put(MString.beforeIndex(part, ':'), MString.afterIndex(part, ':'));
				} else {
					lText.add(part);
				}
			}
		}
		this.text = lText.toArray(new String[lText.size()]);
	}
	
	public String[] getGeneralFilters() {
		if (text == null) return new String[0];
		return text;
	}
	
	public boolean isFiltering() {
		return text != null && text.length > 0 || facets != null && facets.size() > 0;
	}
	
	public String getFacet(String name) {
		if (facets == null) return null;
		return facets.get(name);
	}
	
	public Set<String> getFacetKeys() {
		if (facets == null) return new EmptySet<String>();
		return facets.keySet();
	}

	public IProperties toProperties() {
		return new MProperties(facets);
	}

}
