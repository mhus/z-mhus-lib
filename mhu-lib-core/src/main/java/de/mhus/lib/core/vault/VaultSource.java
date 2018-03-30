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
package de.mhus.lib.core.vault;

import java.util.UUID;

import de.mhus.lib.errors.NotSupportedException;

public interface VaultSource {

	/**
	 * Return a entry by id or null if not found.
	 * 
	 * @param id
	 * @return The id or null
	 */
	VaultEntry getEntry(UUID id);

	/**
	 * Return a list of current stored entry ids.
	 * 
	 * @return a list of ids.
	 */
	UUID[] getEntryIds();
	
	/**
	 * Try to adapt the source to the given class or interface.
	 * @param ifc
	 * @return The requested interface or class.
	 * @throws NotSupportedException Thrown if the source can't be adapted to the interface.
	 */
	<T> T adaptTo(Class<? extends T> ifc) throws NotSupportedException;

	/**
	 * Return a unique name of the source.
	 * 
	 * @return the name
	 */
	String getName();
	
}
