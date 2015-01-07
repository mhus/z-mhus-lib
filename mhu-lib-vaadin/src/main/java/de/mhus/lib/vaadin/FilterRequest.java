package de.mhus.lib.vaadin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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
	
}
