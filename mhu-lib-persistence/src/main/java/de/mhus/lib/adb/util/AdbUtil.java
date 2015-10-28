package de.mhus.lib.adb.util;

import de.mhus.lib.adb.query.AQuery;
import de.mhus.lib.core.MString;

public class AdbUtil {

	private static final SearchHelper DEFAULT_HELPER = new SearchHelper();

	/**
	 * Extend the query by operators from the search string. THe method analyze the search string and
	 * add all search parameters to the query concatenating with 'and'.
	 * The syntax of the search are <key>:<value>,<key>:<value>... It's also possible to set only a value.
	 * In this case the helper is asked for the correct key.
	 * 
	 * The helper plays a central role in analyzing the query. The helper can also deny the usage of keys or
	 * change the name.
	 * 
	 * The value can have place holders at the beginning and end to generate a like statement. The placeholder
	 * is a asterisk.
	 * 
	 * @param query The query to extend
	 * @param search The search string
	 * @param helper The helper or null for the default helper
	 */
	public static void extendObjectQueryFromSearch(AQuery<?> query, String search, SearchHelper helper) {
		if (MString.isEmpty(search)) return;
		if (helper == null) helper = DEFAULT_HELPER;
		String[] parts = search.split(",");
		for (String part : parts) {
			extendObjectQueryFromParameter(query, part, helper);
		}
	}

	public static void extendObjectQueryFromParameter(AQuery<?> query, String part, SearchHelper helper) {
		int p = part.indexOf(':');
		String key = null;
		String value = null;
		if (p < 0) {
			key = helper.findKeyForValue(query, part);
			value = part;
		} else {
			key = part.substring(0,p);
			value = part.substring(p+1);
		}
		key = helper.transformKey(query, key);
		if (key == null) return; // ignore
		value = helper.transformValue(query, key, value);
		
		boolean like = false;
		if (helper.isLikeAllowed(query,key)) {
			if (value.startsWith("*") || value.endsWith("*")) {
				value = transformToLikeLike(value);
				like = true;
			}
		}
		
		if (like)
			query.like(key, value);
		else
			helper.extendQuery(query, key, value);
		
	}

	public static String transformToLikeLike(String value) {
		if (value.indexOf('\\') > -1) value = value.replace("\\", "\\\\");
		if (value.indexOf('%') > -1) value = value.replace("%", "\\%");
		if (value.startsWith("*")) value = "%" + value.substring(1);
		if (value.endsWith("*")) value = value.substring(0, value.length()-1) + "*";
		return value;
	}
}
