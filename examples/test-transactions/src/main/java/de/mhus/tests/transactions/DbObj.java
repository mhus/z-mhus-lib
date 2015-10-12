package de.mhus.tests.transactions;

import java.util.UUID;

import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.basics.UuidIdentificable;

public class DbObj extends DbComfortableObject implements UuidIdentificable {
	
	@DbPrimaryKey
	private UUID id;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
}
