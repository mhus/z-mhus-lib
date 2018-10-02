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

import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.util.SecureString;

public class DefaultEntry implements VaultEntry {
	
	protected UUID id = UUID.randomUUID();
	protected String type;
	protected String description;
	protected SecureString value;
	
	public DefaultEntry() {}
	
	public DefaultEntry(UUID id, String type, String description, String value) {
		this(type,description,value);
		this.id = id;
	}
	public DefaultEntry(String type, String description, String value) {
		this.type = type;
		this.description = description;
		this.value = new SecureString(value);
	}
	
	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public SecureString getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return MSystem.toString(this, id, type);
	}

	@Override
	public String getDescription() {
		return description;
	}

}
