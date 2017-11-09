package de.mhus.lib.core.vault;

import java.io.IOException;
import java.util.UUID;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.crypt.AsyncKey;
import de.mhus.lib.core.crypt.MCrypt;
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
	public <T> T adaptTo(Class<? extends T> ifc) throws NotSupportedException, IOException {
		if (ifc == AsyncKey.class && MVault.TYPE_RSA_PRIVATE_KEY.equals(getType())) {
			return (T) MCrypt.loadPrivateRsaKey(getValue());
		}
		if (ifc == AsyncKey.class && MVault.TYPE_RSA_PUBLIC_KEY.equals(type)) {
			return (T) MCrypt.loadPrivateRsaKey(getValue());
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
