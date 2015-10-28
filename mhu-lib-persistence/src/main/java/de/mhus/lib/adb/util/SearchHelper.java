package de.mhus.lib.adb.util;


import de.mhus.lib.adb.query.AQuery;

public class SearchHelper {

	public String findKeyForValue(AQuery<?> query, String value) {
		throw new de.mhus.lib.errors.NotSupportedException(value);
	}

	/**
	 * Validate the key. Throw an error to deny the query or return
	 * null to ignore this key.
	 * 
	 * @param query
	 * @param key
	 * @return
	 */
	public String transformKey(AQuery<?> query, String key) {
		return key;
	}

	public String transformValue(AQuery<?> query, String key, String value) {
		return value;
	}

	public boolean isLikeAllowed(AQuery<?> query, String key) {
		return true;
	}

	public void extendQuery(AQuery<?> query, String key, String value) {
		query.eq(key, value);
	}

}
