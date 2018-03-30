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
import de.mhus.lib.core.crypt.AsyncKey;
import de.mhus.lib.core.crypt.MCrypt;
import de.mhus.lib.core.crypt.pem.PemPriv;
import de.mhus.lib.core.crypt.pem.PemPub;
import de.mhus.lib.core.crypt.pem.PemUtil;
import de.mhus.lib.core.parser.ParseException;
import de.mhus.lib.core.util.SecureString;
import de.mhus.lib.errors.NotSupportedException;

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
	public String getValue() {
		return value.value();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T adaptTo(Class<? extends T> ifc) throws ParseException, NotSupportedException {
		if (getType() != null) {
			try {
				if (ifc == AsyncKey.class && MVault.TYPE_RSA_PRIVATE_KEY.equals(getType())) {
					return (T) MCrypt.loadPrivateRsaKey(getValue());
				}
				if (ifc == AsyncKey.class && MVault.TYPE_RSA_PUBLIC_KEY.equals(type)) {
					return (T) MCrypt.loadPrivateRsaKey(getValue());
				}
				if (ifc == PemPriv.class && getType().endsWith(".cipher.private.key")) {
					return (T) PemUtil.cipherPrivFromString(getValue());
				}
				if (ifc == PemPub.class && getType().endsWith(".cipher.public.key")) {
					return (T) PemUtil.cipherPubFromString(getValue());
				}
				if (ifc == PemPriv.class && getType().endsWith(".sign.private.key")) {
					return (T) PemUtil.signPrivFromString(getValue());
				}
				if (ifc == PemPub.class && getType().endsWith(".sign.public.key")) {
					return (T) PemUtil.signPubFromString(getValue());
				}
			} catch (Exception e) {
				throw new ParseException(e);
			}
		}
		throw new NotSupportedException(this,ifc);
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
