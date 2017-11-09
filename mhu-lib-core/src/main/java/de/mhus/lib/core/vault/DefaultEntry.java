package de.mhus.lib.core.vault;

import java.io.IOException;
import java.util.UUID;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.crypt.AsyncKey;
import de.mhus.lib.core.crypt.MCrypt;
import de.mhus.lib.errors.NotSupportedException;

public class DefaultEntry implements VaultEntry {
	
	protected UUID id = UUID.randomUUID();
	protected String type;
	protected String description;
	protected String value;
	
	public DefaultEntry() {}
	
	public DefaultEntry(String type, String description, String value) {
		this.type = type;
		this.description = description;
		this.value = value;
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
		return value;
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
