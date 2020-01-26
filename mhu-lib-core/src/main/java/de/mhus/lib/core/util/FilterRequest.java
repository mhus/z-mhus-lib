/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.util;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MCast;
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
                if (part.startsWith("'") && part.endsWith("'") && part.length() > 1) {
                    part = MUri.decode(part.substring(1, part.length() - 1));
                }
                int p = part.indexOf(':');
                if (p > 0) {
                    facets.put(part.substring(0, p), part.substring(p + 1));
                } else {
                    lText.add(part);
                }
            }
        }
        this.text = lText.toArray(new String[lText.size()]);
    }

    public String[] getText() {
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

    public Date getFacet(String name, Date def) {
        if (facets == null) return def;
        return MCast.toDate(facets.get(name), def);
    }

    public int getFacet(String name, int def) {
        if (facets == null) return def;
        return MCast.toint(facets.get(name), def);
    }

    public long getFacet(String name, long def) {
        if (facets == null) return def;
        return MCast.tolong(facets.get(name), def);
    }

    public boolean getFacet(String name, boolean def) {
        if (facets == null) return def;
        return MCast.toboolean(facets.get(name), def);
    }

    public Set<String> getFacetKeys() {
        if (facets == null) return new EmptySet<String>();
        return facets.keySet();
    }

    public IProperties toProperties() {
        return new MProperties(facets);
    }

    public boolean isText() {
        return text != null && text.length > 0;
    }

    public boolean isFacet(String key) {
        return facets != null && facets.containsKey(key);
    }
}
