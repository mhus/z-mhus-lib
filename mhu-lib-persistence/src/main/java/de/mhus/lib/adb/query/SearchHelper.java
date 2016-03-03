package de.mhus.lib.adb.query;



/**
 * <p>SearchHelper class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class SearchHelper {

	/**
	 * <p>findKeyForValue.</p>
	 *
	 * @param query a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @param value a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String findKeyForValue(AQuery<?> query, String value) {
		throw new de.mhus.lib.errors.NotSupportedException(value);
	}

	/**
	 * Validate the key. Throw an error to deny the query or return
	 * null to ignore this key.
	 *
	 * @param query a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @param key a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String transformKey(AQuery<?> query, String key) {
		return key;
	}

	/**
	 * <p>transformValue.</p>
	 *
	 * @param query a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @param key a {@link java.lang.String} object.
	 * @param value a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String transformValue(AQuery<?> query, String key, String value) {
		return value;
	}

	/**
	 * <p>isLikeAllowed.</p>
	 *
	 * @param query a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @param key a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean isLikeAllowed(AQuery<?> query, String key) {
		return true;
	}

	/**
	 * <p>extendQuery.</p>
	 *
	 * @param query a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @param key a {@link java.lang.String} object.
	 * @param value a {@link java.lang.String} object.
	 */
	public void extendQuery(AQuery<?> query, String key, String value) {
		query.eq(key, value);
	}

}
