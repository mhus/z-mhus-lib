package de.mhus.lib.adb.util;

import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;

/**
 * <p>Property class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class Property extends DbComfortableObject {

	@DbPrimaryKey
	private String key;
	@DbPersistent
	private String value;

	/**
	 * <p>Getter for the field <code>key</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getKey() {
		return key;
	}
	/**
	 * <p>Setter for the field <code>key</code>.</p>
	 *
	 * @param key a {@link java.lang.String} object.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getValue() {
		return value;
	}
	/**
	 * <p>Setter for the field <code>value</code>.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
