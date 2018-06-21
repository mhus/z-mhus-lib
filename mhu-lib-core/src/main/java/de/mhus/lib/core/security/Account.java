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

	boolean isSynthetic();

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

	/**
	 * Return the set of assigned groups
	 * 
	 * @return List of groups
	 * @throws NotSupportedException 
	 */
	 String[] getGroups() throws NotSupportedException;
	
}
