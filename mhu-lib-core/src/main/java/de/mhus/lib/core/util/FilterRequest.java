package de.mhus.lib.core.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import de.mhus.lib.core.MString;

/**
 * <p>FilterRequest class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class FilterRequest {

	private Map<String, String> facets;
	private String[] text;

	/**
	 * <p>Constructor for FilterRequest.</p>
	 *
	 * @param text an array of {@link java.lang.String} objects.
	 * @param facets a {@link java.util.Map} object.
	 */
	public FilterRequest(String[] text, Map<String, String> facets) {
		this.text = text;
		this.facets = facets;
	}
	
	/**
	 * <p>Constructor for FilterRequest.</p>
	 *
	 * @param text a {@link java.lang.String} object.
	 */
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
	
	/**
	 * <p>getGeneralFilters.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getGeneralFilters() {
		if (text == null) return new String[0];
		return text;
	}
	
	/**
	 * <p>isFiltering.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isFiltering() {
		return text != null && text.length > 0 || facets != null && facets.size() > 0;
	}
	
	/**
	 * <p>getFacet.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getFacet(String name) {
		if (facets == null) return null;
		return facets.get(name);
	}
	
}
