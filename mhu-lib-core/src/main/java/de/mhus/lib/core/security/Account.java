package de.mhus.lib.core.security;

import de.mhus.lib.core.IReadProperties;
import de.mhus.lib.errors.NotSupportedException;

public interface Account extends Rightful {
	
	String MAP_ADMIN = "admin";
	String ACT_READ = "read";
	String ACT_CREATE = "create";
	String ACT_UPDATE = "update";
	String ACT_MODIFY = "modify";
	String ACT_DELETE = "delete";

	boolean isValid();

	boolean validatePassword(String password);

	boolean isSyntetic();

	String getDisplayName();
	
	/**
	 * Gets all attributes of the user account.
	 * If attributes are not supported return null;
	 * 
	 * @return all attributes in read only properties list or null if properties are not supported.
	 */
	IReadProperties getAttributes();

	/**
	 * Overwrites the given attributes, leaves all other properties untouched. Use
	 * NullValue to remove a attribute from list.
	 * 
	 * @param properties
	 * @throws NotSupportedException Thrown if attributes are not supported
	 */
	void putAttributes(IReadProperties properties) throws NotSupportedException;
	
}
