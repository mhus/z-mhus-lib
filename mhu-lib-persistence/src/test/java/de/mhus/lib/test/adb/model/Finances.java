package de.mhus.lib.test.adb.model;

import java.util.UUID;

import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.annotations.adb.DbTable;

@DbTable(features="accesscontrol")
public class Finances extends DbComfortableObject {

	private UUID id;
	private UUID store;
	private double activa;
	private double passiva;
	private String confidential;
	private String newConfidential;

	@DbPrimaryKey
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}

	@DbPersistent
	public double getActiva() {
		return activa;
	}
	public void setActiva(double activa) {
		this.activa = activa;
	}

	@DbPersistent
	public double getPassiva() {
		return passiva;
	}
	public void setPassiva(double passiva) {
		this.passiva = passiva;
	}

	@DbPersistent
	public void setStore(UUID shop) {
		this.store = shop;
	}
	public UUID getStore() {
		return store;
	}

	@DbPersistent
	public void setConfidential(String confidential) {
		this.confidential = confidential;
		this.newConfidential = null;
	}
	public String getConfidential() {
		return confidential;
	}

	public void setNewConfidential(String string) {
		newConfidential = string;
	}
	public String getNewConfidential() {
		return newConfidential;
	}


}
